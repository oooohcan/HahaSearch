@echo off
pip install -r ./spider/requirements.txt || echo "No execution pip install"
start python ./spider/FlaskApp.py || echo "python spider/FlaskApp.py error"
start ./elastic/bin/elasticsearch.bat || echo "No execution elasticsearch"
