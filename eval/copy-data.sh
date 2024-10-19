docker cp 10MB.txt namenode:/tmp/
docker cp 100MB.txt namenode:/tmp/
docker cp 200MB.txt namenode:/tmp/
docker cp 300MB.txt namenode:/tmp/
docker cp 400MB.txt namenode:/tmp/
docker cp 500MB.txt namenode:/tmp/

docker exec -it namenode bash
hdfs dfs -mkdir -p /user/root
hdfs dfs -mkdir -p /user/root/input
hdfs dfs -mkdir -p /user/root/output

cd /tmp
hdfs dfs -put 10MB.txt /user/root/input
hdfs dfs -put 100MB.txt /user/root/input
hdfs dfs -put 200MB.txt /user/root/input
hdfs dfs -put 300MB.txt /user/root/input
hdfs dfs -put 400MB.txt /user/root/input
hdfs dfs -put 500MB.txt /user/root/input


docker cp /home/ec2-user/lineage/spark/eval_examples/target/eval_examples-3.5.3.jar namenode:/tmp/
docker exec -it namenode bash
cd tmp
hdfs dfs -put eval_examples-3.5.3.jar /user/root