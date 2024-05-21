# Workshop Service Management

This repository contains samples for the workshop Service Management.

## Demos

You find the demos in the corresponding branches

* [Demo Backstage](https://github.com/openknowledge/workshop-service-management/tree/backstage)
* [Demo Observability](https://github.com/openknowledge/workshop-service-management/tree/observability)
* [Demo Service Mesh](https://github.com/openknowledge/workshop-service-management/tree/service-mesh)
* [Demo Versioning V1.0](https://github.com/openknowledge/workshop-service-management/tree/versioning-v1.0)
* [Demo Versioning V1.1](https://github.com/openknowledge/workshop-service-management/tree/versioning-v1.1)
* [Demo Versioning V2.0](https://github.com/openknowledge/workshop-service-management/tree/versioning-v2.0)

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
In this case also check, if the ingresses are created properly. It may be that the 
Grafana and Prometheus ingresses are missing, because the operator helm chart was installed, but 
not the ingresses because of the missing Ingress-Operator.
If that happened, uninstall the helm chart and run `skaffold run` again to reinstall the helm chart.

```shell
    helm uninstall -n observability kube-prometheus-stack
    skaffold run
```

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
127.0.0.1       prometheus.localhost
127.0.0.1       grafana.localhost
127.0.0.1       jaeger.localhost
```

#### NodePorts

If you don't have admin rights or do not want to change the /etc/hosts file, the applications
can be accessed via the exposed NodePorts with the following assigned ports:

```
For Address Validation: http://localhost:30080
For Billing: http://localhost:30081
For Customer: http://localhost:30082
For Delivery: http://localhost:30083
For Prometheus: http://localhost:30090
For Grafana: http://localhost:30030
For Jaeger: http://localhost:30091
```

#### Port-Forwarding

If for whatever reason both options above are not working, you can also use port-forwarding to
access the applications.

### Check what is running

To check what is running on the cluster, you can use kubectl to navigate through the cluster.
k9s.io is also a nice tool to do so.

## "Fixing" the missing sidecar container for OpenTelemetry

After everything has been installed on the cluster via Skaffold, you will notice that there is
only one container in each pod for the micro-services (address-validation, billing, customer, delivery).
This is because the Operator did not have the chance to boot up properly, before skaffold applied
the kustomize files to the cluster.

To get the sidecars for the pods injected, just delete the pods. The deployment will be triggered
automatically and the pods will be recreated with the sidecar containers, because the operator should
be up and running by then.

## Accessing the Grafana Dashboard

To access the Grafana Dashboard, you can use the following credentials: admin/admin
Skip the password change and you will be redirected to the dashboard.

In the Dashboard list, search for "Tracing". That is our pre-build Dashboard, which
visualizes the 4 Services with their traces, and the corresponding logs.

To actually see traffic on that dashboard, trigger some requests to the services.
For example: 
    
```shell
curl --location --request GET 'localhost:30083/delivery-addresses/0815'
```

Or to use multiple microservices to see the propagation you can change the address with a POST

```shell
curl --location --request POST 'localhost:30083/delivery-addresses/0815' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "city": "26122 Oldenburg",
        "recipient": "Max Mustermann",
        "street": {
        "name": "Musterstrasse",
        "number": "22"
    }
}'
```

Now you should see some traces in the Grafana Tracing Dashboard.

## Aufsetzen des Clusters auf dem Azure Cluster

- Readme anpassen: #workshop #api-management
    - az login
    - az account set --subscription <sub-id>
    - cd terraform
    - terraform init
    - terraform apply
        - Optional: Wenn in neuer subscription - Quota Limit der Standard Bs Family erhöhen via Request in der Azure Platform. 4 Quota werden pro Cluster in unserem default setup benötigt. Wir benutzen 2 Nodes der Standard B2s VM Size die jeweils 2 Quota verbrauchen. Bei 5 Clustern ist also ein min. Quota Limit von 20 notwendig.
        - workshop namen eingeben
    - Wildcard A Records im Azure Portal manuell eintragen indem die externe IP aus dem Service des ip routing ingress controllers vom jeweiligen Cluster ausgelesen wird.
    - Unter deployment einen stage Ordner anlegen (bzw. von einem vorherigen workshop kopieren) und so viele unterordner wie cluster benötigt werden
    - Der Name für den ACR muss geupdated werden
        - Dies muss in den Ingress Patches passieren
        - und in der skaffold.yaml
    - Falls gewünscht auch die Ingresses an folgenden Stellen updaten. (bspw. wenn die domain nicht mehr *.api-workshop-0... sonder *.my-workshop-0 heißen soll)
        - deployment/<stage>/<cluster>/patches/ingresses
        - ./kube-prometheus-stack-values.yaml
        - deployment/base/observability/jaeger
    - via azure portal aks connect befehl kopieren und ausführen um die lokale kubeconfig erweitern
        - z.B. az aks get-credentials --resource-group rg-workshop-apidesigncamp --name workshop-cluster-apidesigncamp-1 --overwrite-existing
    - in der skaffold yaml die profiles anpassen auf die neuen contexts und paths zur stage anpassen.
    - az acr login --name <acr-name>
    - skaffold run ausführen
        - k config use-context <context>
        - skaffold run
    - Am Ende nochmal auf allen Clustern die anwendungspods im production namespace neustarten, damit der otel-collector injected wird. (danach sind 2/2 container ready statt 1/1)


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
