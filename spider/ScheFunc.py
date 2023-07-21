from flask_apscheduler import APScheduler

from TenantPool import TenantPool

sched = APScheduler()
tenantPool = TenantPool()


def check_pool():
    '''
    调度任务，检查所有租户的任务池
    '''
    global tenantPool
    tenantPool.check_task()
