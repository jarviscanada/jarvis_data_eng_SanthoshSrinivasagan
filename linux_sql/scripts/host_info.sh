psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ $# -ne  5 ]; then
  echo "Insufficient arguments, please follow the format"
  echo "host_info.sh <psql_host> <psql_port> <db_name> <psql_user> <psql_password>"
  exit 1
fi


hostname=$(hostname -f)
lscpu_out=$(lscpu)

#Retrieve hardware specification variables
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk '{$1="";$2="";print}' | xargs)
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | xargs)
totalMem=$(cat /proc/meminfo | egrep "^MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(date +%Y-%m-%d" "%H:%M:%S)

#Update database with hardware information
insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp )
             VALUES('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, ${l2_cache%K}, $totalMem,'$timestamp')"

export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?