sudo apt-get update
sudo apt-get install ca-certificates curl gnupg
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/debian/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian \
  "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update

sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin git uidmap

dockerd-rootless-setuptool.sh install

git clone https://github.com/txrm/webserver-sample.git


# Build and run the webserver-sample Docker container
cd webserver-sample
docker build -t webserver-sample .
docker run -d -p 8000:8000 --pid=host webserver-sample


if ! docker images | grep -q "nginx"; then
    docker pull nginx:latest
fi

if [ "$(docker ps -q -f name=nginx-proxy)" ]; then
    echo "The nginx proxy container is already running."
else
    docker run -d -p 80:80 --name nginx-proxy --link webserver-sample:webserver nginx
fi

echo "Setup completed."
