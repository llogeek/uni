# 1 task
avian <- read.csv(file = "C:/Users/Лолита/Downloads/avianHabitat.csv", sep =",")
height<- avian$AHt
height<-height[height != 0]
h2<- avian$HHt
h2<-h2[h2 != 0]
# 2 task
print("Max value")
max(height)
print("Min value")
min(height)
print("Mean value")
mean(height)
print("Mediana")
median(height)
print("Mode")
getmode<-function(v){
  uniq<-unique(v)
  uniq[which.max(tabulate(match(v, uniq)))]
}
result<-getmode(v = height)
result
print("Dispersion")
var(height)
print("standard deviation")
sd(height)
print("first and third quartile")
quantile(height, c(0.25, 0.75))
# 3 task
print("Diagram (4 variant)")
boxplot(height)
# 4 task
print("Diagram (4 and 5 variant)")
boxplot(height, h2)
# 5 task
print("Function")
plot.ecdf(height)
# 6 task
hist(height, breaks = 20, freq = FALSE, col = "lightblue",
     xlab = "Данные",
     ylab = "Плотность",
     main = "Гистограмма AHt")
lines(density(height), col = "red", lwd = 2)
#7 task
qqnorm((height-mean(height))/sd(height))
lines(c(-4,4), c(-4,4), lwd = 3, col = "#00FA9A")

