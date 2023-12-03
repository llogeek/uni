getwd()
setwd("D:/lolita/university/5semester/ds_analys")
path = "D:/lolita/university/5semester/ds_analys/res.txt"
getwd()
#install.packages("xlsxjars")
#install.packages("MASS")
#install.packages("matlib")
library("matlib")
library("MASS")
library("random")
options(max.print = 100000)
 
# task 1

sigma <- function(){
  x1 <- c(runif(1, 0.5, 1), runif(1, -1, -0.5), runif(1,-1, -0.5))
  x2 <- c(runif(1, -1, -0.7), runif(1, 0.8, 1), runif(1, 0.8, 1))
  x3 <- c(runif(1, -1, -0.6), runif(1, 0.9, 1), runif(1,0.9, 1))
  sigma <- rbind(x1, x2, x3)
  return(sigma)
}
my_mu <- function(){
  x <- sample(-5:5, size = 3)
  return(x)
} 
mu <- my_mu()
mu
sgm <- sigma()
sgm
print(sgm)
m_sigm <-  sgm %*% t(sgm)
print(m_sigm)
print(mu)
df <- as.data.frame(mvrnorm(n = 1410, mu = mu, Sigma = m_sigm))
print(df)
cor(df)

# task 2

el_num <- c(sample(500:1410, 1))
del <- df[el_num, ]
df <- df[-el_num, ]
print(df)
print(del)

# task 3 

v1 <- rep(1, length(df$x1))
matr_x <- matrix(c(v1, df$x2, df$x3), ncol = 3)
y <- df$x1
matr_xt_x <- t(matr_x) %*% matr_x
matr_xt_x_inv <- solve(matr_xt_x)
matr_xt_y <- t(matr_x) %*% y
A <- matr_xt_x_inv %*% matr_xt_y
print(A) # a0, a2, a3
u_3 <- sample(NA, length(df$x1), TRUE)
for (i in 1:length(df$x1)){
  u_3[i] = df$x1[i] - A[1,1] - A[2, 1] * df$x2[i] - A[3, 1] * df$x3[i]
}
#print(u_3)
mean(u_3) # мат. ожидание
var(u_3) # дисперсия

# task 4
mist <- sample(NA, 1410, TRUE)
y_1 <- sample(NA, 1410, TRUE)
for (i in 1:1410){
  y_1[i] = A[1,1] + A[2, 1] * df$x2[i] + A[3, 1] * df$x3[i]
}
for (i in 1:1410){
  mist[i] = (df$x1[i] - y_1[i])*(df$x1[i] - y_1[i])
}
print(sum(mist, na.rm = TRUE) / length(mist))

# task 5
result <- c()
task_5 <- function(sgm, mu){
  mse <- sample(0, 100, TRUE)
  for (i in 1:100){
    m_sigm <- sgm %*% t(sgm)
    df <- as.data.frame(mvrnorm(n = 1410, mu = mu, Sigma = m_sigm))
    el_num <- c(sample(500:1410, 1))
    del <- df[el_num, ]
    df <- df[-el_num, ]
    
    v1 <- rep(1, length(df$x1))
    matr_x <- matrix(c(v1, df$x2, df$x3), ncol = 3)
    y <- df$x1
    matr_xt_x <- t(matr_x) %*% matr_x
    matr_xt_x_inv <- solve(matr_xt_x)
    matr_xt_y <- t(matr_x) %*% y
    A <- matr_xt_x_inv %*% matr_xt_y
    u <- sample(NA, length(df$x1), TRUE)
    for (j in 1:length(df$x1)){
      u[j] = df$x1[j] - A[1,1] - A[2, 1] * df$x2[j] - A[3, 1] * df$x3[j]
    }
    mist <- sample(0, 1410, TRUE)
    y_1 <- sample(0, 1410, TRUE)
    for (j in 1:1410){
      y_1[j] = A[1,1] + A[2, 1] * df$x2[j] + A[3, 1] * df$x3[j]
    }
    for (j in 1:1410){
      if (!is.na(df$x1[j]))
        mist[j] = (df$x1[j] - y_1[j])*(df$x1[j] - y_1[j])
        mse[i] = mse[i] + mist[j]
    }
    temp = mse[i]
    mse[i] <- temp / length(mist)
  }
  print(sum(mse, na.rm = TRUE) / 100)
}
task_5(sgm, mu)

# task 6

lm(formula = df $x1 ~ df$x2 + df$x3)

# task 7

matr_x <- matrix(c(df$x2, df$x3), ncol = 2)
y <- df$x1
matr_xt_x <- t(matr_x) %*% matr_x
matr_xt_x_inv <- solve(matr_xt_x)
matr_xt_y <- t(matr_x) %*% y
A <- matr_xt_x_inv %*% matr_xt_y
print(A) # a2, a3
u <- sample(NA, length(df$x1), TRUE)
for (i in 1:length(df$x1)){
  u[i] = df$x1[i] - A[1, 1] * df$x2[i] - A[2, 1] * df$x3[i]
}
mean(u) # мат. ожидание
var(u) # дисперсия

mist <- sample(NA, 1410, TRUE)
y_1 <- sample(NA, 1410, TRUE)
for (i in 1:1410){
  y_1[i] = A[1, 1] * df$x2[i] + A[2, 1] * df$x3[i]
}
for (i in 1:1410){
  mist[i] = (df$x1[i] - y_1[i])*(df$x1[i] - y_1[i])
}
print(sum(mist, na.rm = TRUE) / length(mist))

# task 8

mu1 <- my_mu()
sgm1 <- sigma()
print(sgm1)
m_sigm1 <-  sgm1 %*% t(sgm1)
print(m_sigm1)
print(mu1)
df1 <- as.data.frame(mvrnorm(n = 1410, mu = mu1, Sigma = m_sigm1))
print(df1)
cor(df1)
plot(df1$x1, df1$x2, col="red", 
     xlab="x1", 
     ylab = "x2", 
     main = "x1, x2", 
     asp = 1)

v1 <- rep(1, length(df1$x1))
matr_x <- matrix(c(v1, df1$x2), ncol = 2)
y <- df1$x1
matr_xt_x <- t(matr_x) %*% matr_x
matr_xt_x_inv <- solve(matr_xt_x)
matr_xt_y <- t(matr_x) %*% y
A <- matr_xt_x_inv %*% matr_xt_y
print(A) # a0, a2
u <- sample(NA, length(df1$x1), TRUE)
for (i in 1:length(df1$x1)){
  u[i] = df1$x1[i] - A[1,1] - A[2, 1] * df1$x2[i] 
}
mean(u) # мат. ожидание
var(u) # дисперсия

x1_8 <- sample(NA, 1410, TRUE)
for (i in 1:1410){
  x1_8[i] = A[1,1] + A[2, 1] * df1$x2[i]
}
lm(formula = df1$x1 ~ df1$x2)
abline(lm(df1$x1 ~ df1$x2))
# //////////////////////////////////////////////////////////////////////////////////////////////////////
# task 9
u_9 <- sample(NA, 1410, TRUE)
for (i in 1:1410){
  u_9[i] = df$x1[i] - A[1,1] - A[2, 1] * df$x2[i]
}

# task 10
myhist = hist(u_3,col = "red",breaks = "Sturges", na.rm = TRUE)
N <- length(u_3)
print(N)
tochki = myhist$breaks
tochki
M = length(tochki)
M
h = (max(tochki) - min(tochki))/(M - 1)
h
# # Теперь можем подсчитать коэффициент (масштабный множитель) #
koeff = N*h
# # Построим кривую плотности и совместим её с гистограммой #
curve(dnorm(x,mean(u_3),sd(u_3))*koeff,min(tochki),max(tochki),col = "blue",lwd = 3,add = TRUE)
ni = myhist$counts
ni
# Проверим, совпадает ли сумма эмпирических частот с объёмом выборки #
mysumE = sum(ni)
mysumE
# # Найдём ТЕОРЕТИЧЕСКИЕ частоты попадания в интервалы #
for (i in 1:M - 1) {
      pi[i] = pnorm(tochki[i + 1],mean(u_3, na.rm = TRUE),sd(u_3, na.rm = TRUE)) - pnorm(tochki[i],mean(u_3, na.rm =  TRUE),sd(u_3, na.rm = TRUE))
      print(pi[i]);
}  
# # Для контроля подсчитаем сумму теоретических вероятностей #
mysumT = sum(pi, na.rm = TRUE)
mysumT
# # Вычислим значение критерия Хи-квадрат # Это - мера отличия эмпирических частот от теоретических # (в идеале, это значение должно равняться 0) #
myChisq = 0
K = length(ni)
for (i in 1:K) {
  chisl = ni[i] - N*pi[i];
  chisl = chisl*chisl;
  chisl = chisl/(N*pi[i]);
  myChisq = myChisq + chisl;
} 
print("Вычисленное значение критерия Хи-квадрат = ")
myChisq 

# task 11

# task 12
for (k in 3:50){
  
}
# task 13






