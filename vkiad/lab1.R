#порядковый номер в группе
i <- 5
i
# 1 task
(v1 <- seq(i, 6.5, length.out = nchar("Гарматная")))
v1

namelength <- nchar("Лолита")
first<-abs(13-i)+2
step <- (100/first)^(1/(namelength-1))
(v2 <- first * (step ^ (seq(0, namelength - 1))))
v2

m=matrix(c(v2,v1),nrow=3);
m
# 2 task
mydf <- data.frame(numbers = c(1,3,0.4,5,6,7), logic  = c(T,T,F,T,F,T), f)
f <- rbinom(6, 3, 0.4)
f <- factor(f)
mydf <- cbind(mydf, factor = f)
mydf 
mydf[mydf$logic == TRUE, c("numbers", "logic", "f")]

# 3 task

 flowers <- read.csv(file = "C:/Users/Лолита/Downloads/iris.csv", sep =",")
 sum(flowers$Sepal.Width[flowers$Species == "setosa"] <= 3)
 sum(flowers$Sepal.Width[flowers$Species == "versicolor"] <=3)
 sum(flowers$Sepal.Width[flowers$Species == "virginica"] <=3)
 #sum(flowers$Sepal.Width <= 3)
 sum(flowers$Petal.Length > 4.5)
 sum(flowers$Sepal.Width <= 3 & flowers$Petal.Length > 4.5)
 mean(flowers$Petal.Width[flowers$Species == "setosa"])
 mean(flowers$Petal.Width[flowers$Species == "versicolor"])
 mean(flowers$Petal.Width[flowers$Species == "virginica"])