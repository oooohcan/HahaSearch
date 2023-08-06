import threading
from pathlib import Path

from Tenant import Tenant
from Tasks import BasicTask
from Config import FILE_PATH


class TenantPool:
    def __init__(self):
        self.tenants = dict()
        self.lock = threading.Lock()

    def check_tenant(self, code: str):
        with self.lock:
            return code in self.tenants.keys()

    def add_tenant(self, code: str):
        with self.lock:
            if not code in self.tenants.keys():
                self.tenants[code] = Tenant(code)
        tenant_dir = Path(FILE_PATH) / str(code)
        if not tenant_dir.exists():
            tenant_dir.mkdir()

    # def get_tenant(self, code: str):
    #     with self.lock:
    #         if code in self.tenants.keys():
    #             return self.tenants[code]
    #     return None

    def check_task(self):
        with self.lock:
            for it in self.tenants.values():
                it: Tenant
                it.check_pool()

    def get_tasks(self, code: str) -> dict:
        with self.lock:
            if code in self.tenants.keys():
                return self.tenants[code].get_all_task()
        raise Exception('租户不存在')

    def get_running_tasks(self, code: str) -> dict:
        with self.lock:
            if code in self.tenants.keys():
                return self.tenants[code].get_running_task()
        raise Exception('租户不存在')

    def get_waiting_tasks(self, code: str) -> dict:
        with self.lock:
            if code in self.tenants.keys():
                return self.tenants[code].get_waiting_task()
        raise Exception('租户不存在')

    def add_task(self, code: str, task: BasicTask):
        with self.lock:
            if code in self.tenants.keys():
                try:
                    self.tenants[code].add_task(task)
                    return
                except Exception as e:
                    raise e
        raise Exception('租户不存在')

    def pause_task(self, code: str, index: int):
        with self.lock:
            if code in self.tenants.keys():
                try:
                    self.tenants[code].pause_task(index)
                    return
                except Exception as e:
                    raise e
        raise Exception('租户不存在')

    def resume_task(self, code: str, index: int):
        with self.lock:
            if code in self.tenants.keys():
                try:
                    self.tenants[code].resume_task(index)
                    return
                except Exception as e:
                    raise e
        raise Exception('租户不存在')

    def cancel_task(self, code: str, index: int):
        with self.lock:
            if code in self.tenants.keys():
                try:
                    self.tenants[code].cancel_task(index)
                    return
                except Exception as e:
                    raise e
        raise Exception('租户不存在')
