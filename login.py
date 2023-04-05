from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import time



postLogin_actual_Url="https://revpro2-devops-angdb01.revpro.cloud/#/dashboard/home"
# create instance of Chrome webdriver
driver = webdriver.Chrome()
driver.get("https://revpro2-devops-angdb01.revpro.cloud/#/account/login")
time.sleep(5)

# find the element where we have to
# enter the xpath
# fill the number or mail
driver.find_element("xpath", '//*[@id="username"]').send_keys('Pratik')

# find the element where we have to
# enter the xpath
# fill the password
driver.find_element("xpath", '//*[@id="password"]').send_keys('F54wc%3qfi')

# find the element log in
# request using xpath
# clicking on that element
driver.find_element("xpath", '//*[@id="loginSubmitButton"]').click()


time.sleep(5)
postLogin_current_Url = driver.current_url
assert str(postLogin_current_Url) == str(postLogin_actual_Url), "Login Unsuccessful"
print("Login Success")

