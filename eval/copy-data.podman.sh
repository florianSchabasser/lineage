podman cp 100MB.txt namenode:/tmp/
podman cp 200MB.txt namenode:/tmp/
podman cp 300MB.txt namenode:/tmp/
podman cp 400MB.txt namenode:/tmp/
podman cp 500MB.txt namenode:/tmp/

podman exec -it namenode bash
hdfs dfs -mkdir -p /user/root
hdfs dfs -mkdir -p /user/root/input
hdfs dfs -mkdir -p /user/root/output

cd /tmp
hdfs dfs -put 100MB.txt /user/root/input
hdfs dfs -put 200MB.txt /user/root/input
hdfs dfs -put 300MB.txt /user/root/input
hdfs dfs -put 400MB.txt /user/root/input
hdfs dfs -put 500MB.txt /user/root/input


podman cp /home/ec2-user/lineage/spark/eval_examples/target/eval_examples-3.5.3.jar namenode:/tmp/
podman exec -it namenode bash
cd tmp
hdfs dfs -put eval_examples-3.5.3.jar /user/root