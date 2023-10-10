# Workshop Service Management

This repository contains samples for the workshop Service Management.

## Demos

You find the demos in the corresponding branches

* [Demo Client](https://github.com/openknowledge/workshop-api-management/tree/client)
* [Demo Backstage](https://github.com/openknowledge/workshop-api-management/tree/backstage)
* [Demo Monitoring](https://github.com/openknowledge/workshop-api-/tree/monitoring)
* [Demo Versioning V1.0](https://github.com/openknowledge/workshop-api-management/tree/versioning-v1.0)
* [Demo Versioning V1.1](https://github.com/openknowledge/workshop-api-management/tree/versioning-v1.1)
* [Demo Versioning V2.0](https://github.com/openknowledge/workshop-api-management/tree/versioning-v2.0)

## Setting up a Cluster

Create a Kubernetes Cluster on your machine:

### KinD
If you want to use a Kubernetes in Docker Cluster, you can use [KinD](https://kind.sigs.k8s.io/docs/user/quick-start).

To do so create a cluster by executing the following command:

```shell
kind create cluster --config=./deployment/cluster-setup/kind-config.yml --name=workshop-service-mngmt-cluster
```

Check that context is set to kind-workshop-service-mgmt-cluster:

```shell
kubectl config current-context
```

If the context was not set automatically, but the cluster is running in the docker environment 
set the context manually:

### Certificates for LinkerD

Install Step and run the following commands.
Override the existing certificates in the process.
```shell
step certificate create root.linkerd.cluster.local ca.crt ca.key --profile root-ca --no-password --insecure

step certificate create identity.linkerd.cluster.local issuer.crt issuer.key --profile intermediate-ca --not-after 8760h --no-password --insecure --ca ca.crt --ca-key ca.key
```

```shell
kubectl config set-context kind-workshop-service-mgmt-cluster
```
## Deploying to the Cluster via Skaffold

To deploy the applications to the cluster, we use [Skaffold](https://skaffold.dev/).

Skaffold will build the required images and deploy them to the cluster via kustomize.
If the cluster created, Skaffold and kubectl are installed, we are ready to go.

To deploy the applications to the cluster, execute the following command in the root directory 
of this repository:

```shell
skaffold run
```

### Known Issues with the Ingress Operator

We observed that sometimes it can happen, that the ingress-operator is not installed quick enough.
If that happens just rerun the skaffold run command after it failed the first time.

### Accessing the Applications

To access the Applications we can either use the ingresses that are deployed to the cluster or
access them via the exposed NodePort Services. Port-Forwarding is also an option, but not
necessary.

#### Ingresses

To access them via a Domain Name add those entries to the /etc/hosts file.
(You will need admin rights to do so)

```
127.0.0.1       address-validation.localhost
127.0.0.1       billing.localhost
127.0.0.1       customer.localhost
127.0.0.1       delivery.localhost
```

#### NodePorts

If you don't have admin rights or do not want to change the /etc/hosts file, the applications
can be accessed via the exposed NodePorts with the following assigned ports:

```
For Address Validation: http://localhost:30080
For Billing: http://localhost:30081
For Customer: http://localhost:30082
For Delivery: http://localhost:30083
```

#### Port-Forwarding

If for whatever reason both options above are not working, you can also use port-forwarding to
access the applications.

### Check what is running

To check what is running on the cluster, you can use kubectl to navigate through the cluster.
k9s.io is also a nice tool to do so.

## Cleaning up the cluster

To clean up the cluster from everything that skaffold has installed, execute the following command:

```shell
skaffold delete
```

To also delete the KinD cluster and the docker container that it is running in,
execute the following commands:

```shell
docker container stop workshop-service-mngmt-cluster-control-plane
kind delete cluster -n workshop-service-mngmt-cluster
```
