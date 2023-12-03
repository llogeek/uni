getwd()
setwd("D:/lolita/university/5semester/ds_analys")
# task 0
df <- read.table("var3.txt", sep = '\n', header = FALSE)
#print(df)

# task 1

# 1.а
plot(df$V1, type = 'p', main = "График a")
#plot(1:500, df$V1)
#library(ggplot2)
#qplot(1:500, df$V1, main="График реализации")

# 1.б
boxplot(df)

# 1.в (для того чтобы гистограммы показывали частоты встречаемости убери или поменяй значение freq;
       #freq = FALSE гистограммы показывают плотность вероятности каждого класса )
hist(df$V1, breaks = seq(min(df$V1), max(df$V1), length.out = 4 ), main = "Гистограмма 1.в.1", col = "lightblue")
hist(df$V1,  main = "Гистограмма 1.в.2", col = "lightgreen")
hist(df$V1, breaks = seq(min(df$V1), max(df$V1), length.out = 31 ),  main = "Гистограмма 1.в.3", col = "lightyellow")
# думаю, что это однородное распределение; 
#3-я диаграмма с 30 разбиениями больше похожа на гребень

# task 2
#summary(df$V1)
m <- mean(df$V1) # среднее
m
s_dev <- sd(df$V1)   # стандартное отклонение
s_dev
var(df$V1) # дисперсия
median(df$V1) # медиана
quantile(df$V1, 0.25) #первая квартиль
quantile(df$V1, 0.75) #третья квартиль
library(moments)
skewness(df$V1, na.rm = TRUE) #коэффициент асимметрии
kurtosis(df$V1, na.rm = TRUE) #коэффициент эксцесса
min(df$V1) # минимум
max(df$V1) # максимум
# task 3
r_border <- m + (3 * s_dev)
l_border <- m - (3 * s_dev)
count <- 0
for (i in 1:500)
  if (df$V1[i] < r_border && df$V1[i] > l_border) {
    count <- count + 1
  }
p_1 <- count / 500
print(p_1)

#task 4, 5
# проверка гипотезы о нормальном распределении
myhist = hist(df$V1,col = "red",breaks = "Sturges")
N <- length(df$V1)
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
curve(dnorm(x,mean(df$V1),sd(df$V1))*koeff,min(tochki),max(tochki),col = "blue",lwd = 3,add = TRUE)
ni = myhist$counts
ni
# Проверим, совпадает ли сумма эмпирических частот с объёмом выборки #
mysumE = sum(ni)
mysumE
# # Найдём ТЕОРЕТИЧЕСКИЕ частоты попадания в интервалы #
for (i in 1:M - 1) {
  pi[i] = pnorm(tochki[i + 1],mean(df$V1),sd(df$V1)) - pnorm(tochki[i],mean(df$V1),sd(df$V1));
  print(pi[i]);
}  
# # Для контроля подсчитаем сумму теоретических вероятностей #
mysumT = sum(pi)
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
# # Выясним, насколько велико вычисленное значение критерия Хи-квадрат # Найдем вероятность превышения этого значения случайной величиной, # имеющей распределение Хи-квадрат с силом степеней свободы = K-3

fd = K - 3
prob = 1 - pchisq(myChisq,fd)
prob
# осталось сравнить найденную вероятность с 0,05 : 0 < 0.05
# ////////////////////////////////////////////////////////////////////////////////////////////////////////////
# проверка гипотезы о равномерном распределении
a = mean(df$V1) - sqrt(3) * sd(df$V1)
b = mean(df$V1) + sqrt(3) * sd(df$V1)
curve(dunif(x,mean(df$V1),sd(df$V1))*koeff,min(tochki),max(tochki),col = "blue",lwd = 3,add = TRUE)
# # Найдём вероятность попадания случайной величины в интервалы (x_i, x_i+1) #
for (i in 1:M - 1) 
{
  pi[i] = (tochki[i + 1] - tochki[i]) / (b - a);
  print(pi[i]);
}  
xi_2 <- 0
for (i in 1:K) {
  chisl = ni[i] - N*pi[i];
  chisl = chisl*chisl;
  chisl = chisl/(N*pi[i]);
  xi_2 = xi_2 + chisl;
} 
print("Вычисленное значение критерия Хи-квадрат = ")
xi_2
# # Выясним, насколько велико вычисленное значение критерия Хи-квадрат
# Найдем вероятность превышения этого значения случайной величиной, 
# имеющей распределение Хи-квадрат с силом степеней свободы = K-3 (14.1)
fd_1 = K - 3
fd_1
prob_1 = 1 - pchisq(xi_2,fd_1)
prob_1

#task 6
num = 0.3 #runif(1, 0.2, 0.5)
#num
num = num * 500
#num
df[sample(1:nrow(df), num), "V1"] = NA
#print(df)
#sum(!complete.cases(df))

# task 7
mean(df$V1[!is.na(df$V1)]) # среднее
sd(df$V1[!is.na(df$V1)])   # стандартное отклонение
var(df$V1[!is.na(df$V1)]) # дисперсия
median(df$V1[!is.na(df$V1)]) # медиана
quantile(df$V1[!is.na(df$V1)], 0.25) #первая квартиль
quantile(df$V1[!is.na(df$V1)], 0.75) #третья квартиль
library(moments)
skewness(df$V1[!is.na(df$V1)], na.rm = TRUE) #коэффициент асимметрии
kurtosis(df$V1[!is.na(df$V1)], na.rm = TRUE) #коэффициент эксцесса
min(df$V1[!is.na(df$V1)]) # минимум
max(df$V1[!is.na(df$V1)]) # максимум

# task 8
fill_m = mean(df$V1, na.rm = TRUE)
df$V1[is.na(df$V1)] = fill_m
mean(df$V1) # среднее
sd(df$V1)   # стандартное отклонение
var(df$V1) # дисперсия
median(df$V1) # медиана
quantile(df$V1, 0.25) #первая квартиль
quantile(df$V1, 0.75) #третья квартиль
library(moments)
skewness(df$V1) #коэффициент асимметрии
kurtosis(df$V1) #коэффициент эксцесса
min(df$V1) # минимум
max(df$V1) # максимум

# task 9
for (i in 1:500){
  if (is.na(df$V1[i])) df$V1[i] = (df$V1[i-1] + df$V1[i+1]) / 2
}
mean(df$V1)
sd(df$V1)   # стандартное отклонение
var(df$V1) # дисперсия
median(df$V1) # медиана
quantile(df$V1, 0.25) #первая квартиль
quantile(df$V1, 0.75) #третья квартиль
library(moments)
skewness(df$V1) #коэффициент асимметрии
kurtosis(df$V1) #коэффициент эксцесса
min(df$V1) # минимум
max(df$V1) # максимум

# task 10
df1 <- read.table("var3.txt", sep = '\n', header = FALSE)
df1
deltaa = (max(df1) + 2 * min(df$V1)) / 3
deltaa
df1$V1[df1$V1 < deltaa] = NA
df1

# task 11
mean(df1$V1, na.rm = TRUE) # среднее
sd(df1$V1, na.rm = TRUE)   # стандартное отклонение
var(df1$V1, na.rm = TRUE) # дисперсия
median(df1$V1, na.rm = TRUE) # медиана
quantile(df1$V1, 0.25, na.rm = TRUE) #первая квартиль
quantile(df1$V1, 0.75, na.rm = TRUE) #третья квартиль
library(moments)
skewness(df1$V1, na.rm = TRUE) #коэффициент асимметрии
kurtosis(df1$V1, na.rm = TRUE) #коэффициент эксцесса
min(df1$V1, na.rm = TRUE) # минимум
max(df1$V1, na.rm = TRUE) # максимум

# task 12
fill_m1 = mean(df1$V1, na.rm = TRUE)
df1$V1[is.na(df1$V1)] = fill_m1
mean(df1$V1) # среднее
sd(df1$V1)   # стандартное отклонение
var(df1$V1) # дисперсия
median(df1$V1) # медиана
quantile(df1$V1, 0.25) #первая квартиль
quantile(df1$V1, 0.75) #третья квартиль
library(moments)
skewness(df1$V1) #коэффициент асимметрии
kurtosis(df1$V1) #коэффициент эксцесса
min(df1$V1) # минимум
max(df1$V1) # максимум

# task 13
datf <- read.table("var3.txt", sep = '\n', header = FALSE)
#datf
max_13 = max(datf$V1)
min_13 = min(datf$V1)
L <- min_13 + max_13
left = L + max_13
right = min_13 - L
for (i in 1:5) {
  datf <- rbind(datf, sample(right:(right + i/10), 1));
  datf <- rbind(datf, sample((-L):left, 1))
}
#datf
mean(datf$V1) # среднее
sd(datf$V1)   # стандартное отклонение
var(datf$V1) # дисперсия
median(datf$V1) # медиана
quantile(datf$V1, 0.25) #первая квартиль
quantile(datf$V1, 0.75) #третья квартиль
library(moments)
skewness(datf$V1) #коэффициент асимметрии
kurtosis(datf$V1) #коэффициент эксцесса
min(datf$V1) # минимум
max(datf$V1) # максимум

# task 14
data <- read.table("var3.txt", sep = '\n', header = FALSE)
data
max_14 = max(data)
min_14 = min(data)
st = L + max_14
L = min_14 + max_14
for (i in 1:10) {
  datf <- rbind(datf, sample(st:(st + i/10) , 1));
}
mean(data$V1) # среднее
sd(data$V1)   # стандартное отклонение
var(data$V1) # дисперсия
median(data$V1) # медиана
quantile(data$V1, 0.25) #первая квартиль
quantile(data$V1, 0.75) #третья квартиль
library(moments)
skewness(data$V1) #коэффициент асимметрии
kurtosis(data$V1) #коэффициент эксцесса
min(data$V1) # минимум
max(data$V1) # максимум






