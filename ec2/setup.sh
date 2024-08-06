sudo yum install git -y
sudo yum install docker -y
sudo service docker start
sudo usermod -aG docker ec2-user
# logout and login again to re-evaluate the group membership
docker --version
# Make docker autostart
sudo chkconfig docker on

# Get docker-compose
sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
# Set permissions
sudo chmod +x /usr/local/bin/docker-compose
# Verify success
docker-compose version


git clone https://github.com/big-data-europe/docker-hadoop.git


# SSH Port Forwarding
# Hadoop UI
ssh -i "ssh_key.pem" -N -T -L 9870:0.0.0.0:9870 ec2-user@ec2-35-159-67-249.eu-central-1.compute.amazonaws.com
# Hadoop Namenode
ssh -i "ssh_key.pem" -N -T -L 9000:localhost:9000 ec2-user@ec2-35-159-67-249.eu-central-1.compute.amazonaws.com
# Hadoop Datanode
ssh -i "ssh_key.pem" -N -T -L 9866:localhost:9866 ec2-user@ec2-35-159-67-249.eu-central-1.compute.amazonaws.com
# Kafka UI
ssh -i "ssh_key.pem" -N -T -L 9001:localhost:9001 ec2-user@ec2-35-159-67-249.eu-central-1.compute.amazonaws.com
# Kafka
ssh -i "ssh_key.pem" -N -T -L 9094:localhost:9094 ec2-user@ec2-35-159-67-249.eu-central-1.compute.amazonaws.com
# Neo4j
ssh -i "ssh_key.pem" -N -T -L 7474:localhost:7474 ec2-user@ec2-35-159-67-249.eu-central-1.compute.amazonaws.com
ssh -i "ssh_key.pem" -N -T -L 7687:localhost:7687 ec2-user@ec2-35-159-67-249.eu-central-1.compute.amazonaws.com

