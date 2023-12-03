getwd()
setwd("D:/lolita/university/5semester/ds_analys")
getwd()

# task 1

options(max.print=1000000)
ch <- read.table(file = "var3.csv", header = TRUE, sep = ";")
ch
vec0 <- gsub(',', '.', ch$class)
vec1 <- gsub(',', '.', ch$x.1)
vec2 <- gsub(',', '.', ch$x.2)
vec3 <- gsub(',', '.', ch$x.3)
vec4 <- gsub(',', '.', ch$x.4)
suppressWarnings(class<-as.numeric(vec0))
suppressWarnings(x1<-as.numeric(vec1))
suppressWarnings(x2<-as.numeric(vec2))
suppressWarnings(x3<-as.numeric(vec3))
suppressWarnings(x4<-as.numeric(vec4))
df <- data.frame(class, x1, x2, x3, x4)
df
# task 2

el_num <- c(sample(1:150, 15))
print(el_num)
del <- df[el_num,]
df <- df[-el_num,]
#print(df)
print(del)

# task 3

Y <- matrix(0:0, nrow = 135, ncol = 3)
for (i in 1:135){
  Y[i, df$class[i]] = 1
}
tmp <- c(df$x1, df$x2, df$x3, df$x4)
X <- matrix(tmp, nrow = 135, ncol = 4)
B <- (solve(t(X) %*% X)) %*% (t(X) %*% Y)

count <- 0
m <- matrix(c(del$x1, del$x2, del$x3, del$x4), nrow = 15, ncol = 4)
for (i in 1:15){
  class_i <- which.max(t(m[i,]) %*% B)
  if (class_i == del$class[i]){
    count <- count + 1
  }
}
mistakes <- 15 - count
print(mistakes)

# task 4 

k <- 3;

count <- 0;
for(j in 1: 15) {
  distances <- c()
  classes <- c()
  
  for(i in 1:135) {
    d <- sqrt((df$x1[i] - del$x1[j])^2 + (df$x2[i] - del$x2[j])^2
              + (df$x3[i] - del$x3[j])^2 + (df$x4[i] - del$x4[j])^2)
    distances[i] <- d
    classes[i] <- df$class[i]
    
  }
  df1 <- data.frame(distances, classes)
  df1 <- df1[order(df1$distance),]
  inds <- c()
  for(i in 1:k){
    inds[i] <- df1$classes[i]
  }
  uniqv <- unique(inds)
  class1 <- uniqv[which.max(tabulate(match(inds, uniqv)))]
  if(class1 == del$class[j]) {
    count <- count + 1
  }
}
count_knn3 <- 15 - count;

k <- 5;
count <- 0;

for(j in 1: 15) {
  distances <- c()
  classes <- c()
  
  for(i in 1:135) {
    d <- sqrt((df$x1[i] - del$x1[j])^2 + (df$x2[i] - del$x2[j])^2
              + (df$x3[i] - del$x3[j])^2 + (df$x4[i] - del$x4[j])^2)
    distances[i] <- d
    classes[i] <- df$class[i]
    
  }
  df1 <- data.frame(distances, classes)
  df1 <- df1[order(df1$distance),]
  inds <- c()
  for(i in 1:k){
    inds[i] <- df1$classes[i]
  }
  uniqv <- unique(inds)
  class1 <- uniqv[which.max(tabulate(match(inds, uniqv)))]
  if(class1 == del$class[j]) {
    count <- count + 1
  }
}
count_knn5 <- 15 - count

print(count_knn3)
print(count_knn5)

# task 5

library(ggplot2);
ggplot(data = df, aes(x = x1, y = x2)) + geom_point(aes(color = factor(class), shape = factor(class))) + labs(title = "Scatter plot", x = "x1", y = "x2")

# task 6
###############################################################################################
Y <- matrix(0:0, nrow = 135, ncol = 3)
for (i in 1:135){
  Y[i, df$class[i]] = 1
}
tmp <- c(df$x1, df$x2)
X <- matrix(tmp, nrow = 135, ncol = 2)
B <- (solve(t(X) %*% X)) %*% (t(X) %*% Y)

count <- 0
print(B)
m <- matrix(c(del$x1, del$x2), nrow = 15, ncol = 2)
for (i in 1:15){
  class_i <- which.max(t(m[i,]) %*% B)
  if (class_i == del$class[i]){
    count <- count + 1
  }
}
mistakes <- 15 - count
print(mistakes)
install.packages("psych")
library(psych)
