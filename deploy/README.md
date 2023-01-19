# Dockerfile

Archivo que contiene las instrucciones para armar la imagen de contenedor a utilizar por parte del despliegue. Más información sobre como crear este archivo: [Dockerfile](https://docs.docker.com/engine/reference/builder/#:~:text=A%20Dockerfile%20is%20a%20text,can%20use%20in%20a%20Dockerfile%20.)

# values.yml

Archivo que maneja las variables a ser pasadas a los objetos desplegados sobre kubernetes. A continuación se muestra un diagrama y la interrelación de los objetos más usados de Kubernetes.

![alt text](https://support.huaweicloud.com/intl/en-us/basics-cce/en-us_image_0258869759.png)

En esta primera etapa, el pipeline utilizará y desplegara de manera automática los siguientes objetos:

- [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/)
- [Service](https://kubernetes.io/es/docs/concepts/services-networking/service/)
- [Deployment](https://kubernetes.io/es/docs/concepts/workloads/controllers/deployment/)
- [Secrets](https://kubernetes.io/es/docs/concepts/configuration/secret/)
- [HPA](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/)

## Uso del archivo values.yml

El archivo posee una estructura tipo YAMLo, en el siguiente ejemplo la variable sería "namespace". 

```yaml
namespace: plusd
```
en este otro ejemplo, las variables a configurar serían "url" y "path" las cuales estan sobre la sección "ingress". Es decir ingress es el nombre de agrupación de variables y no una variable, por ello esta no debe tener datos.

```yaml
ingress:
  url: desarrollo.oefa.gob.pe
  path: /plusd/backend
```

Asi mismo tener en cuenta que los datos de las variables a aconfigurar **NO DEBEN ESTAR ENTRE COMILLAS SIMPLES O DOBLES** 

## Variables

A continuación se describe la funcionalidad de cada una de las variables mandatorias.

- **name:** Nombre de los objetos de Kubernetes a desplegar.
- **namespace:** Nombre del espacio de trabajo (namespace) sobre kubernetes donde se desplegará el microservicio.
- **version**: Versión (tag) del microservicio a desplegar. Valor por defecto "latest".
- **ingress.url:** URL donde irá desplegada la aplicación
- **ingress.path:** Path o contexto donde se expondrá la aplicación, por ejemplo "/my/app".
- **ingress.rewrite:** Habilitar o deshabilitar la reescritura de path hacia "/", es decir que se elimine el path en la petición antes de enviarla a los pods (true|false).
- **ingress.PathPrefixStrip:** Habilitar o deshabilitar PathPrefixStrip (true|false).
- **deployment.image:** Imagen utilizada en el despliegue.
- **deployment.memory:** Memoria utilizada por cada pod en megabytes (Mi), por ejemplo 100 Megabytes serían "100Mi".
- **deployment.cpu:** CPU utilizada por cada pod en microcores (m), por ejemplo un cuarto de core sería "250m", un core completo sería "1000m", etc. 
- **deployment.port:** Puerto en el cual escucha(n) el pod.
- **deployment.protocol:** Protocolo del puerto en el cual escucha el pod (TCP|UDP). Por defecto será TCP.
- **secrets:** En esta sección irá toda la lista de variables usada por los pod y su correspondiente valor, uno por línea bajo el siguiente formato:
```yaml
secrets:
  DB_CONNECTION_TYPE: SERVICE_NAME
  DB_CONNECTION_VALUE: dvoefabd
  DB_HOST: 10.6.0.15
  DB_PASSWORD: mypassword
  DB_PORT: 1532
  DB_USERNAME: myuser
```

Existen variables adicionales de uso avanzado, las cuales no son obligatorias y pueden ser configuradas en la medida que se necesiten. 

- **hpa.enabled:** Habilitar o deshabilitar la creación de hpa (true|false).
- **hpa.minReplicas:** Mínimo número de pods a levantar.
- **hpa.maxReplicas:** Máximo número de pods a escalar.
- **hpa.targetCPUUtilizationPercentage:** Porcentaje de utilización del CPU asignado antes de escalar un pod adicional, se sugiere el valor 80.
- **hpa.targetMemoryUtilizationPercentage:** Porcentaje de memoria del CPU asignado antes de escalar un pod adicional.
- **actuatorport.enable:** Habilitar o deshabilitar la creación del puerto adicional (true|false).
- **actuatorport.port:** Puerto del actuator.
- **livenessProbe:enable:** Habilitar o deshabilitar la creación del livenessProbe.
- **readinessProbe:enable:** Habilitar o deshabilitar la creación del readinessProbe.
- **startupProbe:enable:** Habilitar o deshabilitar la creación del startupProbe.
- **serviceMonitor.enable:** Habilitar o deshabilitar la creación del serviceMonitor.

Para el caso de habilitar la creación de livenessProbe, readinessProbe o startupProbe, se debe agregar la configuración de estos en formato YAML en cada una de sus secciones correspondiente. Más detalle sobre [livenessProbe, readinessProbe y startupProbe.](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)

En conclusión Un archivo values.yml debería verse de la siguiente forma minimamente:

```yaml
# Nombre de los objetos a desplegar.
name: backend
# Nombre del espacio de trabajo (namespace) sobre kubernetes donde se desplegará el microservicio.
namespace: prueba
# Versión (tag) del microservicio a desplegar. Valor por defecto "latest".
version: latest

# Configuración del Ingress a nivel del Kubernetes (https://kubernetes.io/docs/concepts/services-networking/service/)
ingress:
  # URL donde irá desplegada la aplicación.
  url: desarrollo.oefa.gob.pe
  # Path o contexto donde se expondrá la aplicación, por ejemplo "/my/app".
  path: /prueba
  # Habilitar o deshabilitar la reescritura de path hacia "/", es decir que se elimine el path en la petición antes de enviarla a los pods (true|false).
  rewrite: 
    enabled: true

deployment:
  # Imagen utilizada en el despliegue.
  image: $CI_REGISTRY_IMAGE
  # Memoria utilizada por cada pod en megabytes (Mi), por ejemplo 100 Megabytes sería "100Mi". 
  memory: 500Mi
  # CPU utilizada por cada pod en microcores (m), por ejemplo un cuarto de core sería "250m", un core completo sería "1000m", etc.
  cpu: 500m
  # Puerto en el cual escucha el pod.
  port: 9092
  # Protocolo del puerto en el cual escucha el pod (TCP|UDP). Por defecto será TCP.
  protocol:

# Creación de Secrets a nivel de Kubernetes (https://kubernetes.io/es/docs/concepts/configuration/secret/)
secrets:
  DB_CONNECTION_TYPE: SERVICE_NAME
  DB_CONNECTION_VALUE: dvoefabd
  DB_HOST: 10.6.0.15
  DB_PASSWORD: mypassword
  DB_PORT: 1532
  DB_USERNAME: myuser
```
