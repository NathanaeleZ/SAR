## global

Ce package met en connexion différentes tâches via un channel et leur permet de s'échanger des données.


### Broker

Le broker a un nom qui sert à l'indentifier.
Le broker est un intermédiaire qui permet de donner à 2 tâches qui veulent communiquer entre elles un channel.
Pour établir une connexion, un Broker prend en paramètre un nom (le broker avec qui il veut communiquer) et un numéro de port.
L'autre Broker qui reçoit la demande connection doit être en attente dans accept avec son numéro de port déjà indiqué.
Les numéros de ports doivent être identique de chaque côté sinon exception.

### Task

Chaque tâche est associé à un broker.
C'est la tâche qui donne les informations à son Broker (name et port)
C'est la tâche qui gère les accept et/ou les connect via le Channel que son broker lui a donné

### Connect et accept

Les méthodes connect et accept sont bloquantes.
Connect est bloquant jusqu'à ce que sa demande de connexion soit accepté.
Accept est bloquant jusqu'à ce qu'il recoive une demande de connexion après cela il créé le channel demandé.
S'il y a une exception pendant un accept ou un connect, on arrête l'exécution de la tâche.

### Channel

Le channel est bidirectionnel.
Un channel est utilisé pour 2 tâches uniquement
Aux travers du Channel on envoie des flux d'octets, FIFO et loss less.
Une fois la connexion établie chaque Broker renvoie le channel aux tâches pour que les 2 tâches puissent communiquer entre elles.

### write

Pour communiquer entre elle, les tâches écrivent des données sur un tableau de bytes via la méthode write.
La tâche met des données dans un tableau de bytes et précise la taille
la méthode write n'est pas bloquante.
write renvoie le nombre d'octet qui a été écrit.
Donc si tout n'a pas été écris soit le channel a été déconnecté soit il était plein.
Après il faudra renvoyer le reste d'octets si l'autre n'est pas déconnecté.
Si l'autre est dénnocté alors on arrête de write et on abandonne le channel

#### Paramètres de write
- **offset** position de départ des données (là ou il faut commencer la copie)
- **lenght** nombre de bytes des données qu'on écrit
- **bytes** tableau dans lequel les données sont déjà écrites et on les copie dans le tableau du channel


### read

La méthode read renvoie le nombre d'octets qui ont été écrit.
Quand la tâche effectue un read on suppose qu'il y a des conventions pour savoir combien d'octets lire.
La méthode read n'est pas bloquante donc si 
Quand une tâche read si elle ne reçoit pas le bon nombre d'octets attendu alors elle verifie la connexion.
Si le channel est déconnecté alors la tâche abandonne le channel et si toujours connecté on essaye de read de nouveau pour lire la suite.

#### Paramètres de read
- **offset** position dont ou les données lu seront placé dans le tableau de bytes local
- **lenght** nombre de bytes qu'on veut lire
- **bytes**  tableau dans lequel les données seront écrites


### Déconnexion
Les tâches peuvent se déconnecter d'un channel.
Les tâches peuvent demander si le channel a été déconnecté ou non.
