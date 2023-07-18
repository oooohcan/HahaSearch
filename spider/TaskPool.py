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
                task.paused = False
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
                    return True
            for it in self.running:
                if it.index == index:
                    it.cancel_task()
                    self.ids.append(index)
                    self.running.remove(it)
                    return True
        return False

    def resume_task(self, index: int):
        with self.lock:
            for it in self.waiting:
                it: BasicTask
                if it.index == index:
                    if len(self.running) >= MAX_RUNNING:
                        return False
                    it.resume_task()
                    self.running.append(it)
                    self.waiting.remove(it)
                    return True
        return False

    def pause_task(self, index: int):
        with self.lock:
            for it in self.running:
                it: BasicTask
                if it.index == index:
                    it.pause_task()
                    self.running.remove(it)
                    self.waiting.append(it)
                    return True
        return False
