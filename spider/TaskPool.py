import threading
from private import MAX_JOB, MAX_RUNNING
from Tasks import BasicTask


class TaskPool:
    def __init__(self):
        self.lock = threading.Lock()
        self.waiting = []
        self.running = []
        self.ids = [i for i in range(0, MAX_JOB)]

    def get_index(self):
        with self.lock:
            if len(self.ids) == 0:
                return -1
            return self.ids.pop(0)

    def get_task(self, index: int):
        with self.lock:
            it: BasicTask
            for it in self.waiting:
                if it.index == index:
                    return it
            for it in self.running:
                if it.index == index:
                    return it
        return None

    def get_all_task(self):
        with self.lock:
            return self.waiting + self.running

    def get_running_task(self):
        with self.lock:
            return self.running

    def get_waiting_task(self):
        with self.lock:
            return self.waiting

    def add_task(self, task: BasicTask):
        with self.lock:
            if len(self.waiting)+len(self.running) >= MAX_JOB or task.index in self.ids:
                return False
            self.waiting.append(task)
            if len(self.running) < MAX_RUNNING:
                task.waiting = False
                self.running.append(task)
                self.waiting.remove(task)
            task.start_task()
        return True

    def cancel_task(self, index: int):
        with self.lock:
            for it in self.waiting:
                it: BasicTask
                if it.index == index:
                    it.cancel_task()
                    self.ids.append(index)
                    self.waiting.remove(it)
                    print(f'任务{it.index}已被人为取消')
                    return True
            for it in self.running:
                if it.index == index:
                    it.cancel_task()
                    self.ids.append(index)
                    self.running.remove(it)
                    print(f'任务{it.index}已被人为取消')
                    return True
        return False

    def continue_task(self, index: int):
        # 唤醒因执行队列满而阻塞的进程
        with self.lock:
            for it in self.waiting:
                it: BasicTask
                if it.index == index:
                    if len(self.running) >= MAX_RUNNING:
                        return False
                    if not it.paused:
                        it.continue_task()
                        self.running.append(it)
                        self.waiting.remove(it)
                    return True
        return False

    def resume_task(self, index: int):
        # 唤醒人为阻塞的进程
        with self.lock:
            for it in self.waiting:
                it: BasicTask
                if it.index == index:
                    it.resume_task()
                    return True
            for it in self.running:
                it: BasicTask
                if it.index == index:
                    it.resume_task()
                    return True
        return False

    def pause_task(self, index: int):
        # 人为阻塞某个进程
        with self.lock:
            for it in self.running:
                it: BasicTask
                if it.index == index:
                    it.pause_task()
                    self.running.remove(it)
                    self.waiting.append(it)
                    print(f'任务{it.index}被人为阻塞')
                    return True
            for it in self.waiting:
                it: BasicTask
                if it.index == index:
                    it.pause_task()
                    print(f'任务{it.index}被人为阻塞')
                    return True
        return False

    def check_pool(self):
        with self.lock:
            for it in self.running:
                it: BasicTask
                if it.is_finished():
                    self.running.remove(it)
                    self.ids.append(it.index)
                    print(f'任务{it.index}已完成',
                          f'任务{it.index}进度{it.get_progress()}')
                elif it.paused:
                    self.running.remove(it)
                    self.waiting.append(it)
                    print(f'任务{it.index}被人为阻塞',
                          f'任务{it.index}进度{it.get_progress()}')
            for it in self.waiting:
                it: BasicTask
                if len(self.running) < MAX_RUNNING:
                    if not it.paused:
                        it.continue_task()
                        self.running.append(it)
                        self.waiting.remove(it)
                        print(f'任务{it.index}已开始')
                    else:
                        print(f'任务{it.index}被人为阻塞')
