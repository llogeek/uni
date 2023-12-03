funi<- function(x){
  cos(x)
}
countintegral<-function(funk, a, b, e){
eps<-10^(-e-1)
base<- b-a
result = ((funk(a)+funk(b))/2)*base
n<-1
s<-0
while(abs(result - s) >= eps) {
  s<-result
  base<-base/2
  n<-n*2
  i<-1
  result = result/2
  while(i < n/2){
    x = a + base*(i*2-1);
    i<-i+1
    result<-result + funk(x)*base
  }
}
result
}

countintegral(funi, a=1, b=5, e = 4)
integrate(funi, lower=1,upper=5)