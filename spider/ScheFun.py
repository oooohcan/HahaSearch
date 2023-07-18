from flask_apscheduler import APScheduler
import threading

from TenantPool import TenantPool

sched = APScheduler()
tenantPool = TenantPool()


def check_pool():
    '''
    调度任务，检查所有租户的任务池
    '''
    global tenantPool
    send_thread = threading.Thread(target=tenantPool.check_task())
    send_thread.start()
