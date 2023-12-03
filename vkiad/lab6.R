
library(klaR)
library(caret)
library(class)
library(e1071)
# task 1
x1 <- rnorm(15,8,4)
y1 <- rnorm(15,8,4)
p1<-data.frame( x1, y1, cl = seq(1, 1, length.out = 15))
length(x)

x2 <- rnorm(15,-5, 4)
y2 <- rnorm(15,-8, 4)
p2<-data.frame(x2, y2, cl = seq(2, 2, length.out = 15))

dat<-data.frame(x = c(p1$x1, p2$x2),y = c(p1$y1 , p2$y2), cl = c(p1$cl, p2$cl))
plot(dat$x, dat$y, col = dat$cl + 2, xlab = "x", ylab = "y",xlim = c(-20, 20), ylim = c(-20, 20))
# task 2
x3 <- rnorm(15,-5,8)
y3 <-rnorm(15,5,1)

p3<-data.frame(x3, y3, cl = seq(0, 0, length.out = 15))

# task 3
p3$cl <- knn(dat, p3, cl=dat[, "cl"])
x4 <- c(p1$x1, p2$x2, p3$x3)
y4 <- c(p1$y1, p2$y2, p3$y3)
datknn <- data.frame(x4, y4, cl = c(p1$cl, p2$cl, p3$cl))
plot(datknn$x4, datknn$y4, col = datknn$cl+2, xlab = "x", ylab = "y", xlim = c(-20, 20), ylim = c(-20, 20))
# task 4
dat$cl<- as.factor(dat$cl)
model <- NaiveBayes(dat$cl~., data = dat)
pr <-predict(model,p3)
p3$cl <-pr$class
plot(x4, y4, col = c(p1$cl, p2$cl, p3$cl)+2, xlab = "X", ylab = "Y", xlim = c(-20, 20), ylim = c(-20, 20))
# task 5
svm_dat <- svm(dat$cl~.,data=dat) 
pred2 <-predict(svm_dat, p3)
p3$cl <-pred2
plot(x4, y4, col = c(p1$cl, p2$cl, p3$cl)+2, xlab = "x", ylab = "y", xlim = c(-20, 20), ylim = c(-20, 20))
