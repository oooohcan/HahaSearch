import threading

from TaskPool import TaskPool
from Tasks import BasicTask


class Tenant:
    def __init__(self, code: str):
        self.lock = threading.Lock()
        self.code = code
        self.taskPool = TaskPool()

    def __hash__(self) -> int:
        return hash(self.code)

    def get_code(self):
        return self.code

    def check_pool(self):
        self.taskPool.check_pool()

    def add_task(self, task: BasicTask):
        try:
            self.taskPool.add_task(task)
        except Exception as e:
            raise e

    def pause_task(self, index: int):
        try:
            self.taskPool.pause_task(index)
        except Exception as e:
            raise e

    def resume_task(self, index: int):
        try:
            self.taskPool.resume_task(index)
        except Exception as e:
            raise e

    def cancel_task(self, index: int):
        try:
            self.taskPool.cancel_task(index)
        except Exception as e:
            raise e

    def get_all_task(self):
        # 返回所有任务的字典
        tasks = self.taskPool.get_all_task()
        task_dic = {}
        for it in tasks:
            it: BasicTask
            task_dic[it.index] = it.get_info()
        return task_dic

    def get_running_task(self):
        # 返回正在运行的任务的字典
        tasks = self.taskPool.get_running_task()
        task_dic = {}
        for it in tasks:
            it: BasicTask
            task_dic[it.index] = it.get_info()
        return task_dic

    def get_waiting_task(self):
        # 返回正在等待的任务的字典
        tasks = self.taskPool.get_waiting_task()
        task_dic = {}
        for it in tasks:
            it: BasicTask
            task_dic[it.index] = it.get_info()
        return task_dic
