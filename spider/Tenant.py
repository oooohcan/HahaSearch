import threading

from TaskPool import TaskPool


class Tenant:
    def __init__(self, code: str):
        self.lock = threading.Lock()
        self.code = code
        self.taskPool = TaskPool()

    def __hash__(self) -> int:
        return hash(self.code)

    def get_pool(self):
        return self.taskPool

    def get_code(self):
        return self.code

    def check_pool(self):
        self.taskPool.check_pool()

    def get_all_task(self):
        return self.taskPool.get_all_task()
