import json
from flask import request, Flask
from flask_cors import CORS

import private
from ScheFun import sched, check_pool

app = Flask(__name__)
app.config.from_object(private)
CORS(app, supports_credentials=True)


@app.route('/create_job', methods=['GET', 'POST'])
def create_job():
    data = request.get_data()
    js = json.loads(data)


@app.route('/pause_job', methods=['GET', 'POST'])
def pause_job():
    data = request.get_data()
    js = json.loads(data)


@app.route('/del_job', methods=['GET', 'POST'])
def del_job():
    data = request.get_data()
    js = json.loads(data)


@app.route('/start_job', methods=['GET', 'POST'])
def start_job():
    data = request.get_data()
    js = json.loads(data)


if __name__ == '__main__':
    sched.add_job(id='check_pool',
                  func=check_pool,
                  trigger='interval',
                  seconds=1,
                  max_instances=4)
    sched.start()
    app.run(debug=True, host="0.0.0.0", port=private.MY_APP_PORT)
