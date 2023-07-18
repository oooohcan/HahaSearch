import threading

from Tenant import Tenant


class TenantPool:
    def __init__(self):
        self.tenants = dict()
        self.lock = threading.Lock()

    def add_tenant(self, tenant: Tenant):
        with self.lock:
            self.tenants[tenant.get_code()] = tenant

    def check_task(self):
        with self.lock:
            for it in self.tenants.values():
                it: Tenant
                it.get_pool().check_pool()
