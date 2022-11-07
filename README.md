# Running the samples of the workshop

The samples run on Minikube.

## Install Kubernetes

Install Kubernetes via Minikube. See the instructions below.

## Information for Windows users

Make sure you're using PowerShell or Bash! Do not use _cmd_!

## Important warning

If you are regularly working with kubernetes and `kubectl` it would be wise to create a backup copy of your kubeconfig file.

This file is located

- on macOS: `~/.kube/config`
- on Linux: `~/.kube/config`
- on Windows: `%UserProfile%/.kube/config`

Minikube should only add a kubectl context configuration and should not mess with existing contexts...but you never know...

## Minikube

Install the dependencies:

- Docker 3.3: <https://docs.docker.com/get-docker/>
- Minikube 1.20.0: <https://minikube.sigs.k8s.io/docs/start/>
- kubectl: either via Minikube (<https://minikube.sigs.k8s.io/docs/handbook/kubectl/>) or directly (<https://kubernetes.io/docs/tasks/tools/>)

Start Docker first, then start Minikube. On Windows you'll need to use an administative Terminal to do so.

`minikube start --container-runtime=docker --driver=docker --insecure-registry "10.0.0.0/24"`

### Configure Docker registry

Enable registry addon:

`minikube addons enable registry`

In order to push images which start with "http://localhost:5000/" to Kubernete's own registry, you need to run:

`docker run --rm -it --network=host alpine ash -c "apk add socat && socat TCP-LISTEN:5000,reuseaddr,fork TCP:$(minikube ip):5000"`

This container needs to run the whole time during you run the samples!

### Troubleshooting (macOS)

If something does not work as expected, try to delete Minikube:

Remove the binary: `sudo rm -rf /usr/local/bin/minikube`

Remove the config files: `sudo rm -rf ~/.minikube`

