from django.db import models

# Create your models here.
class Request(models.Model):
	id_request = models.CharField(max_length=120,blank=False)
	img = models.CharField(max_length=250,blank=False)
	ip = models.CharField(max_length=120,default='127.0.0.1')
	mobile = models.TextField(default='Android',blank=True,null=True)
	user_id = models.IntegerField(default = 0)
	preds = models.CharField(max_length=80,default='0')