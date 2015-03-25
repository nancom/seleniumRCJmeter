export pid=`ps aux | grep selenium | awk 'NR==1{print $2}' | cut -d' ' -f1`;kill -9 $pid
export pid=`ps aux | grep selenium | awk 'NR==1{print $2}' | cut -d' ' -f1`;kill -9 $pid
