import pathlib

from ServeTools import response_success, response_wrong
from ScheFunc import tenantPool
from Tasks import BasicTask, HttpTask, FtpTask
from Config import FILE_PATH


def create_http_serve(js: dict):
    params = ['code', 'index', 'name', 'target']
    for param in params:
        if param not in js:
            return response_wrong('缺少参数')

    code = js['code']
    index = int(js['index'])
    name = js['name']
    target = js['target']
    deep = 0
    headers = dict()
    if 'deep' in js:
        deep = int(js['deep'])
    if 'headers' in js:
        headers = js['headers']

    save_path = pathlib.Path(FILE_PATH) / str(code) / str(index)
    if not tenantPool.check_tenant(code):
        tenantPool.add_tenant(code)
    try:
        task = HttpTask(index, name, target, save_path, deep, headers)
        tenantPool.add_task(code, task)
        return response_success('任务已添加')
    except Exception as e:
        return response_wrong(str(e))


def create_ftp_serve(js: dict):
    params = ['code', 'index', 'name', 'target', 'visit_dir', 'uname', 'upwd']
    for param in params:
        if param not in js:
            return response_wrong('缺少参数')
    code = js['code']
    index = int(js['index'])
    name = js['name']
    target = js['target']
    visit_dir = js['visit_dir']
    uname = js['uname']
    upwd = js['upwd']
    deep = 0
    if 'deep' in js:
        deep = int(js['deep'])

    save_path = pathlib.Path(FILE_PATH) / str(code) / str(index)
    if not tenantPool.check_tenant(code):
        tenantPool.add_tenant(code)
    try:
        task = FtpTask(index, name, target, uname,
                       upwd, visit_dir, save_path, deep)
        tenantPool.add_task(code, task)
        return response_success('任务已添加')
    except Exception as e:
        return response_wrong(str(e))


def pause_task_serve(js: dict):
    if 'code' not in js or 'index' not in js:
        return response_wrong('缺少参数')

    code, index = js['code'], int(js['index'])
    if not tenantPool.check_tenant(code):
        return response_wrong('租户不存在')
    try:
        tenantPool.pause_task(code, index)
        return response_success('任务已暂停')
    except Exception as e:
        return response_wrong(str(e))


def resume_task_serve(js: dict):
    if 'code' not in js or 'index' not in js:
        return response_wrong('缺少参数')

    code, index = js['code'], int(js['index'])
    try:
        tenantPool.resume_task(code, index)
        return response_success('任务已恢复')
    except Exception as e:
        return response_wrong(str(e))


def cancel_task_serve(js: dict):
    if 'code' not in js or 'index' not in js:
        return response_wrong('缺少参数')

    code, index = js['code'], int(js['index'])
    try:
        tenantPool.cancel_task(code, index)
        return response_success('任务已删除')
    except Exception as e:
        return response_wrong(str(e))


def get_tasks_serve(js: dict):
    if 'code' not in js:
        return response_wrong('缺少参数')
    code = js['code']
    try:
        return response_success(tenantPool.get_tasks(code))
    except Exception as e:
        return response_wrong(str(e))


def get_running_tasks_serve(js: dict):
    if 'code' not in js:
        return response_wrong('缺少参数')
    code = js['code']
    try:
        return response_success(tenantPool.get_running_tasks(code))
    except Exception as e:
        print(e)
        return response_wrong(str(e))


def get_waiting_tasks_serve(js: dict):
    if 'code' not in js:
        return response_wrong('缺少参数')
    code = js['code']
    try:
        return response_success(tenantPool.get_waiting_tasks(code))
    except Exception as e:
        print(e)
        return response_wrong(str(e))


def get_headers_serve():
    return response_success(
        BasicTask.get_headers()
    )
