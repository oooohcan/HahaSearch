import json
from flask import request, Flask
from flask_cors import CORS

import Config
from ScheFunc import sched, check_pool
from FlaskServe import *

app = Flask(__name__)
app.config.from_object(Config)
CORS(app, supports_credentials=True)


@app.route('/create_http', methods=['GET', 'POST'])
def create_http():
    data = request.get_data()
    js = json.loads(data)
    return create_http_serve(js)


@app.route('/create_ftp', methods=['GET', 'POST'])
def create_ftp():
    data = request.get_data()
    js = json.loads(data)
    return create_ftp_serve(js)


@app.route('/pause_task', methods=['GET', 'POST'])
def pause_task():
    data = request.get_data()
    js = json.loads(data)
    return pause_task_serve(js)


@app.route('/cancel_task', methods=['GET', 'POST'])
def cancel_task():
    data = request.get_data()
    js = json.loads(data)
    return cancel_task_serve(js)


@app.route('/resume_task', methods=['GET', 'POST'])
def resume_task():
    data = request.get_data()
    js = json.loads(data)
    return resume_task_serve(js)


@app.route('/get_tasks', methods=['GET', 'POST'])
def get_tasks():
    data = request.get_data()
    js = json.loads(data)
    return get_tasks_serve(js)


@app.route('/get_running_tasks', methods=['GET', 'POST'])
def get_running_tasks():
    data = request.get_data()
    js = json.loads(data)
    return get_running_tasks_serve(js)


@app.route('/get_waiting_tasks', methods=['GET', 'POST'])
def get_waiting_tasks():
    data = request.get_data()
    js = json.loads(data)
    return get_waiting_tasks_serve(js)


@app.route('/get_headers', methods=['GET', 'POST'])
def get_headers():
    return get_headers_serve()


if __name__ == '__main__':
    sched.init_app(app)
    sched.add_job(id='check_pool',
                  func=check_pool,
                  trigger='interval',
                  seconds=1,
                  max_instances=4)
    sched.start()
    app.run(debug=True, host="0.0.0.0", port=Config.MY_APP_PORT)
    # app.run(debug=True, port=Config.MY_APP_PORT)
