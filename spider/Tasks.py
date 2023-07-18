import pathlib
from ServeTools import *
import queue
import requests
import threading
# from time import sleep


class BasicTask:
    def __init__(self, index: int):
        self.total = 0
        self.success = 0
        self.fail = 0
        self.cancelled = False
        self.waiting = True
        self.paused = False
        self.index = index
        # self.condition = threading.Lock()
        self.condition = threading.Condition()

    def start_task(self):
        pass

    def pause_task(self):
        with self.condition:
            self.paused = True

    def resume_task(self):
        with self.condition:
            self.paused = False
            self.condition.notify()

    def cancel_task(self):
        with self.condition:
            self.cancelled = True

    def is_finished(self) -> bool:
        pass

    def get_agents(self) -> list:
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
        return user_agents

    def get_progress(self) -> dict:
        return {
            'total': self.total,
            'success': self.success,
            'fail': self.fail,
            'rate': float(self.success) / float(self.total) if self.total > 0 else 0
        }


class SpiderHtml(BasicTask):

    def __init__(self, index: int, target: str, save_path: str, save_name: str, deep: int = 0, headers: dict = dict()):
        super().__init__(index)

        if len(target) == 0:
            raise ValueError('url is empty')
        if len(save_path) == 0:
            raise ValueError('save_path is empty')
        if len(save_name) == 0 or not check_file_name(save_name):
            raise ValueError('save_name is invalid')
        if deep < 0 or deep > 5:
            raise ValueError('deep is invalid')
        if not isinstance(headers, dict):
            raise ValueError('headers is invalid')

        self.target = target
        self.save_path = save_path
        self.save_name = save_name
        self.deep = deep
        self.headers = headers

    def is_finished(self) -> bool:
        return self.fail + self.success == self.total

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
        file_name = f'{self.save_name}_{self.success}.html'
        file_path = pathlib.Path(self.save_path) / file_name
        if not pathlib.Path(self.save_path).exists():
            pathlib.Path(self.save_path).mkdir(parents=True)
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(html_content)

    def start_task(self):
        # 新建spider_html线程
        t = threading.Thread(target=self.spider_html)
        t.start()

    def spider_html(self):
        '''
        爬取网页的源代码,并保存为html文件
        当网页中存在指向其他网页的链接时,将会递归爬取,直到递归深度达到deep
        html文件将会保存在save_path中,文件名为save_name
        '''
        level = 0
        last = self.target
        q = queue.Queue()
        q.put(self.target)
        while not q.empty() and level <= self.deep:
            with self.condition:
                if self.cancelled:
                    break
                while self.paused:
                    self.condition.wait()

            url = q.get()
            self.total += 1

            # 获取网页内容
            html_content = ''
            try:
                html_content = self.get_html(url)
            except Exception as e:
                print(e)
                self.fail += 1
                continue

            # 保存网页内容为html文件
            self.save_html(html_content)

            links = find_links(html_content)
            temp = ''
            for link in links:
                link = str(link)
                if link.startswith('http'):
                    q.put(link)
                    temp = link

            if url == last:
                level += 1
                last = temp
            self.success += 1

        print(self.get_progress())
