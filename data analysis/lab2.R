getwd()
setwd("D:/lolita/university/5semester/ds_analys")
getwd()
#install.packages("ggplot2")
library(ggplot2)
library(Hmisc)
#install.packages("ggpubr")
library(ggpubr)
#task 1

options(max.print=1000000)
ch <- read.table(file = "var3.csv", header = TRUE, sep = ";")
ch
vec1 <- gsub(',', '.', ch$x1)
vec2 <- gsub(',', '.', ch$x2)
vec3 <- gsub(',', '.', ch$x3)
vec4 <- gsub(',', '.', ch$x4)
suppressWarnings(x1<-as.numeric(vec1))
suppressWarnings(x2<-as.numeric(vec2))
suppressWarnings(x3<-as.numeric(vec3))
suppressWarnings(x4<-as.numeric(vec4))
df<- data.frame(x1, x2, x3, x4)
df
#print(df)
m1 <- mean(df$x1)
m1
m2 <- mean(df$x2)
m2
m3 <- mean(df$x3)
m3
m4 <- mean(df$x4)
m4
cor(df, use = "complete.obs")
corr11 <- cor.test(df$x1, df$x1)
corr11
corr12 <- cor.test(df$x1, df$x2)
corr12
corr13 <- cor.test(df$x1, df$x3)
corr13
corr14 <- cor.test(df$x1, df$x4)
corr14
corr22 <- cor.test(df$x2, df$x2)
corr22
corr23 <- cor.test(df$x2, df$x3)
corr23
corr24 <- cor.test(df$x2, df$x4)
corr24
corr33 <- cor.test(df$x3, df$x3)
corr33
corr34 <- cor.test(df$x3, df$x4)
corr34

#task 2

hist(df$x1, main = "x1", freq = FALSE)
curve(dnorm(x, mean = m1, sd = sd(df$x1)), col = "blue", lwd = 2, add = TRUE)
hist(df$x2, main = "x2", freq = FALSE)
curve(dnorm(x, mean = m2, sd = sd(df$x2)), col = "blue", lwd = 2, add = TRUE)
hist(df$x3, main = "x3", freq = FALSE)
curve(dnorm(x, mean = m3, sd = sd(df$x3)), col = "blue", lwd = 2, add = TRUE)
hist(df$x4, main = "x4",  freq = FALSE)
curve(dnorm(x, mean = m4, sd = sd(df$x4)), col = "blue", lwd = 2, add = TRUE)

# task 3

 # shapiro test p-value > 0.05 -> normally distributedd

shapiro.test(df$x1)
shapiro.test(df$x2)
shapiro.test(df$x3)
shapiro.test(df$x4)

 # kolmogorov - smirnov test p-value > 0.05 -> normally distributedd

ks.test(df$x1, 'pnorm')
ks.test(df$x2, 'pnorm')
ks.test(df$x3, 'pnorm')
ks.test(df$x4, 'pnorm')

# task 4
# очень интересно, подумай 
 
# task 5


p <- ggplot(df, aes(x = x1, y = x2)) +
  geom_point() 
p
# task 6
p +  stat_ellipse(level = 0.9) +
  stat_ellipse(level = 0.95, color = 2) +
  stat_ellipse(level = 0.99, color = 3)
# task 7

p <- ggplot(df, aes(x = x1, y = x3)) +
  geom_point() 
p

p +  stat_ellipse(level = 0.9) +
  stat_ellipse(level = 0.95, color = 2) +
  stat_ellipse(level = 0.99, color = 3)

p <- ggplot(df, aes(x = x1, y = x4)) +
  geom_point() 
p
p +  stat_ellipse(level = 0.9) +
  stat_ellipse(level = 0.95, color = 2) +
  stat_ellipse(level = 0.99, color = 3)

y1<-numeric()
y2<-numeric()
k1 <- 1
k2 <- 6
k3 <- 9
k4 <- 9

# task 8
y <- data.frame(y1 = sample(NA, 400, TRUE), y2 = sample(NA, 400, TRUE))

for(i in 1:400) {
  y$y1[i] = k1 * df$x1[i] + k3 * df$x3[i]
  y$y2[i] = k2 * df$x2[i] + k4 * df$x4[i]
}

# эмпирически
colMeans(y)
cov(y)
cor(y)

# по формуле

k1 * mean(df$x1) + k3 * mean(df$x3)
k2 * mean(df$x2) + k4 * mean(df$x4)

C <- c(k1, 0, 0, k2, k3, 0, 0, k4)
dim(C) <- c(2, 4)
cov(y)
C %*% cov(df) %*% t(C)
# task 9 
p <- 0.3
x5 <- numeric()
a <- sample(0:1, 400, TRUE, c(1 - p, p))
for (i in 1:400){
  x5 <- c(x5, a[i] * df$x1[i] + (1 - a[i]) * df$x2[i])
}
mean(x5)
var(x5)

# task 10
hist(x5, main = "x5", freq = FALSE)
# task 11





 