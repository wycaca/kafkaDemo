tar -xzf kafka_2.13-3.4.0.tgz
mv kafka_2.13-3.4.0 kafka
cd kafka
vim ~/.bash_profile
export PATH=/root/kafka/bin:$PATH
source ~/.bash_profile
kafka-topic.sh
cd /root/kafka/ && mkdir data && mkdir data/zookeeper


vim config/zookeeper.properties
dataDir=/root/kafka/data/zookeeper


zookeeper-server-start.sh config/zookeeper.properties

mkdir data/kafka
vim config/server.properties
log.dirs=/root/kafka/data/kafka
kafka-server-start.sh config/server.properties


listeners=PLAINTEXT://172.25.111.80:9092
advertised.listeners=PLAINTEXT://139.196.59.28:9092


# 创建 topic
bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic test-topic

# 查看 topic
bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

# 生产者
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic topic1
# 消费者
bin/kafka-console-consumer.sh --bootstrap-server iZuf653yzvax5j5lbfa5psZ:9092 --from-beginning --topic topic1



# docker 镜像加速器
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://tm3jxnh5.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker

# 拉取镜像
docker pull provectuslabs/kafka-ui:latest

# 创建启动UI容器
docker-compose up -d kafka-ui