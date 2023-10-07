## Nginx as Reverse Proxy + Sample Webserver

Tested on fresh Debian installation.



### TODO
- [ ] Remake in Ansible
- [ ] Shorten build time, preferably using something else instead of Scala.



Project has a copy of the original script:

```shell
#!/bin/bash

sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg
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
cd webserver-sample
docker build -t webserver-sample:latest .
docker run -d -p 8000:8000 --name webserver-sample-container --pid=host webserver-sample:latest




if ! docker images | grep -q "nginx"; then
    docker pull nginx:latest
fi


if [ "$(docker ps -q -f name=nginx-reverse)" ]; then
    echo "The nginx reverse proxy container is already running."
    docker rm -f nginx-reverse
else
     cat <<'EOF' > nginx-reverse-proxy.conf
 server {
     listen 80;
     access_log /var/log/nginx/access.log;
     error_log /var/log/nginx/error.log;
     location / {
          proxy_pass http://webserver-sample-container:8000;
          proxy_set_header   Host $host;
          proxy_set_header   X-Real-IP $remote_addr;
          proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header   X-Forwarded-Host $server_name;
     }
 }
EOF

    # LINK CONTAINERS
    docker run -d -p 12345:80 --name nginx-reverse -v $(pwd)/nginx-reverse-proxy.conf:/etc/nginx/conf.d/default.conf --link webserver-sample-container:webserver-sample-container nginx
fi

echo "Setup completed."


```