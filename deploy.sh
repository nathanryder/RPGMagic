mvn clean install && cp target/*.jar ~/Documents/1.12/plugins/
rm ~/Documents/1.12/plugins/RPGMagic/spells.yml
rm ~/Documents/1.12/plugins/RPGMagic/inventories.yml
screen -X stuff 'plugman reload RPGMagic\n'

