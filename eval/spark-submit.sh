podman exec -it spark-master-lineage spark-submit \
  --class WordCountLineage \
  --master spark://spark-master-lineage:7077 \
  --conf "spark.rdd.lineage.detailed=false" \
  hdfs://namenode:9000/user/root/eval_examples-3.5.3.jar 10MB.txt 10MB.txt