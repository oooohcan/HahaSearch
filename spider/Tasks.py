from pathlib import Path
import queue
import requests
import threading
from time import sleep
from random import randint
from ftplib import FTP
import json
from datetime import datetime

from ServeTools import *
from Config import ES_ONE, FILE_SIZE, DOC_EX


class BasicTask:
    def __init__(self, index: int, code: str, name: str):
        self.total = 0
        self.success = 0
        self.fail = 0
        self.cancelled = False
        self.waiting = True
        self.paused = False
        self.index = int(index)
        if len(name) == 0 or len(code) == 0:
            raise ValueError('name or code is empty')
        self.name = name
        self.code = code
        # self.condition = threading.Lock()
        self.condition = threading.Condition()

    def start_task(self):
        pass

    def pause_task(self):
        with self.condition:
            self.paused = True

    def continue_task(self):
        with self.condition:
            self.waiting = False
            self.condition.notify()

    def resume_task(self):
        with self.condition:
            try:
                self.paused = False
                self.condition.notify()
            except:
                raise Exception('任务并未被暂停')

    def cancel_task(self):
        with self.condition:
            self.cancelled = True
            self.paused = False
            self.waiting = False
            try:
                self.condition.notify()
            except:
                pass

    def is_finished(self) -> bool:
        return self.total > 0 and self.total == self.success + self.fail

    def get_headers() -> dict:
        user_agents = [
            # chrome
            'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36',
            # firefox
            'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0',
            # safari
            'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Safari/605.1.15',
            # edge
            'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3',
            # android
            'Mozilla/5.0 (Linux; Android 9; SM-G965F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.143 Mobile Safari/537.36'
        ]
        return {'user_agent': user_agents}

    def get_progress(self) -> dict:
        return {
            'total': self.total,
            'success': self.success,
            'fail': self.fail,
            'rate': float(self.success) / float(self.total) if self.total > 0 else 0
        }

    def get_info(self) -> dict:
        pass

    def add_es(self, content: str, date: int, title: str, ftype: str, website: str, code: str):
        def do_post(data: dict):
            jdata = json.dumps(data)
            headers = {'Content-Type': 'application/json'}
            try:
                resopnse = requests.post(ES_ONE, data=jdata, headers=headers)
                resopnse.raise_for_status()
                res_data = resopnse.json()
                if res_data['code'] != 0:
                    raise Exception(res_data['msg'])
            except Exception as e:
                raise e

        data = {
            'content': content,
            'date': date,
            'title': title,
            'type': ftype,
            'website': website,
            "tenantCode": code
        }
        do_post(data)


class HttpTask(BasicTask):

    def __init__(self, index: int, code: str, name: str, target: str, save_path: str, deep: int = 0, headers: dict = dict()):
        super().__init__(index, code, name)

        if len(target) == 0:
            raise ValueError('url is empty')
        if len(str(save_path)) == 0:
            raise ValueError('save_path is empty')
        if len(name) == 0 or not check_file_name(name):
            raise ValueError('save_name is invalid')
        if deep < 0 or deep > 5:
            raise ValueError('deep is invalid')
        if not isinstance(headers, dict):
            raise ValueError('headers is invalid')

        def rand_header():
            agents = BasicTask.get_headers()['user_agent']
            # print('rand_header')
            return {'user_agent': agents[randint(0, len(agents) - 1)]}

        self.target = target
        self.save_path = save_path
        self.save_name = name
        self.deep = deep
        self.headers = headers if len(headers) > 0 else rand_header()

    def get_info(self) -> dict:
        return {
            'index': self.index,
            'name': self.name,
            'property': 'paused' if self.paused else 'waiting' if self.waiting else 'running' if not self.is_finished() else 'finished',
            'total': self.total,
            'success': self.success,
            'fail': self.fail,
            'rate': round(float(self.success) / float(self.total), 3) if self.total > 0 else 0,
            'type': 'http',
            'url': self.target
        }

    def get_html(self, url: str) -> str:
        '''
        爬取指定url的源代码,返回源代码字符串
        '''
        try:
            response = requests.get(url, headers=self.headers)
            response.raise_for_status()
        except requests.exceptions.HTTPError as e:
            raise e
        except:
            raise Exception('error: ' + url)
        return response.text

    def save_html(self, html_content: str):
        # 保存网页内容为html文件
        sname = find_title(html_content)
        if len(sname) == 0:
            sname = self.save_name
        file_name = f'{sname}_{self.success}.html'
        file_path = Path(self.save_path) / file_name
        if not Path(self.save_path).exists():
            Path(self.save_path).mkdir(parents=True)
        try:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(html_content)
        except:
            raise Exception('error: ' + file_name)

    def start_task(self):
        # 新建spider_html线程
        t = threading.Thread(target=self.spider_html)
        t.start()

    def spider_html(self):
        '''
        爬取网页的源代码,并保存为html文件
        当网页中存在指向其他网页的链接时,将会广度遍历爬取,直到层数达到deep
        html文件将会保存在save_path中,文件名为save_name
        '''
        level = 0
        last = self.target
        q = queue.Queue()
        q.put(self.target)
        self.total = 1
        visit = set()
        while not q.empty() and level <= self.deep:
            with self.condition:
                if self.cancelled:
                    break
                while self.waiting:
                    self.condition.wait()
                while self.paused:
                    self.condition.wait()

            url = q.get()

            html_text = ''
            # 获取网页内容
            try:
                html_text = self.get_html(url)
                # 保存网页内容为html文件
                self.save_html(html_text)

                # 添加至ES
                sname = find_title(html_text)
                if len(sname) == 0:
                    sname = self.save_name
                file_name = f'{sname}_{self.success}.html'
                local_path = Path(self.save_path) / file_name
                self.add_es(str(local_path), int(datetime.now().timestamp()),
                            sname, 'html', url, self.code)
            except Exception as e:
                self.fail += 1
                continue

            temp = ''
            if level < self.deep:  # 广度遍历，当且仅当层数小于deep时才会继续遍历
                links = find_links(html_text)
                for link in links:
                    link = str(link)
                    if link.startswith('http'):
                        if link in visit:
                            continue
                        visit.add(link)
                        q.put(link)
                        self.total += 1
                        temp = link

            if url == last:
                level += 1
                last = temp
            self.success += 1

            sleep(randint(1, 3))


class FtpTask(BasicTask):
    def __init__(self, index: int, code: str, name: str, target: str, uname: str, upwd: str, visit_dir: str, save_path: str, deep: int = 0):
        super().__init__(index, code, name)

        if len(str(target)) == 0:
            raise ValueError('url is empty')
        if len(str(save_path)) == 0:
            raise ValueError('save_path is empty')
        if len(name) == 0 or not check_file_name(name):
            raise ValueError('save_name is invalid')
        if len(str(visit_dir)) == 0 or visit_dir[0] != '/':
            raise ValueError('visit_dir is invalid')
        if deep < 0 or deep > 5:
            raise ValueError('deep is invalid')

        self.target = str(target)
        self.uname = str(uname)
        self.upwd = str(upwd)
        self.visit_dir = str(visit_dir)
        self.save_path = str(save_path)
        self.save_name = str(name)
        self.deep = int(deep)

    def get_info(self) -> dict:
        return {
            'index': self.index,
            'name': self.name,
            'property': 'paused' if self.paused else 'waiting' if self.waiting else 'running' if not self.is_finished() else 'finished',
            'total': self.total,
            'success': self.success,
            'fail': self.fail,
            'rate': round(float(self.success) / float(self.total), 3) if self.total > 0 else 0,
            'type': 'ftp',
            'url': self.target
        }

    def start_task(self):
        # 新建spider_ftp线程
        t = threading.Thread(target=self.spider_ftp)
        t.start()

    def spider_ftp(self):
        try:
            ftp = FTP(self.target)
            ftp.login(self.uname, self.upwd)
        except:
            self.total += 1
            self.fail += 1
            return

        q = queue.Queue()
        q.put(self.visit_dir)
        self.total = 1
        level = 0
        last = self.visit_dir

        while not q.empty():
            with self.condition:
                if self.cancelled:
                    break
                while self.waiting:
                    self.condition.wait()
                while self.paused:
                    self.condition.wait()

            dir = q.get()
            temp = ''
            try:
                ftp.cwd(dir)
            except:
                self.fail += 1
                continue

            files = ftp.nlst()
            for file in files:
                remote_path: str = dir + '/' + file
                try:
                    ftp.cwd(remote_path)
                    if level < self.deep:  # 广度遍历，当且仅当层数小于deep时才会继续遍历
                        q.put(remote_path)
                        self.total += 1
                        temp = remote_path
                    ftp.cwd('..')
                except:
                    file_size = ftp.size(remote_path)
                    file_ext = remote_path.rsplit('.', 1)[-1].lower()
                    if file_size < FILE_SIZE or file_ext in DOC_EX:
                        if not check_file_name(file):
                            continue

                        file_mod_time = ftp.sendcmd(f"MDTM {remote_path}")
                        timestamp = int(datetime.strptime(
                            file_mod_time[4:], "%Y%m%d%H%M%S").timestamp())
                        if not Path(self.save_path).exists():
                            Path(self.save_path).mkdir(parents=True)
                        dfile = Path(self.save_path) / file
                        with open(dfile, 'wb') as f:
                            ftp.retrbinary('RETR ' + file, f.write)
                        try:
                            self.add_es(str(dfile), timestamp,
                                        file, 'ftp', self.target+remote_path, self.code)
                        except:
                            pass

            if dir == last:
                level += 1
                last = temp
            self.success += 1
