from TaskPool import TaskPool


class Tenant:
    def __init__(self, code: str):
        self.code = code
        self.taskPool = TaskPool()
