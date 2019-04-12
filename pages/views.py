from django.http import HttpResponse, Http404, JsonResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt, csrf_protect
from django.core.files.storage import FileSystemStorage
from requests.models import Request
import tensorflow as tf
from tensorflow.keras.models import load_model
import cv2
import numpy as np
#import json
# Create your views here.
def home_view(request,*args,**kwargs):
	return HttpResponse("<h3>hey!</h3>")
@csrf_exempt
def predict_view(request,*args,**kwargs):
	if request.method == "POST":
		#GETTING DATA
		u_img2 = request.FILES['file']
		u_mobile = request.POST.get("mobile")
		u_id = request.POST.get("id_user")
		u_ip = request.POST.get("ip")

		fs = FileSystemStorage()
		filename = fs.save("uploads/" + u_img2.name, u_img2)
		uploaded_file_url = fs.url(filename)

		#PREDICTION
		image = cv2.cvtColor(cv2.imread(uploaded_file_url), cv2.COLOR_BGR2RGB)
		image = np.array(image)/127.5 - 1.
		scaled_img = cv2.resize(image, (224, 224))
		scaled_img = np.expand_dims(scaled_img, axis=0)#add batch
		model = load_model('assets/model_mela.h5')
		preds = model.predict(scaled_img)
		#Save Request
		b = Request(id_request="FX1FFFFF",img=uploaded_file_url,ip=u_ip,mobile=u_mobile,user_id=u_id,preds=preds)
		b.save()
		#RETURNS
		my_context = {"image":uploaded_file_url,"pred":str(preds[0][0])}
		return JsonResponse(my_context)
		#return render(request,"predict.html",my_context)
	else: 
		return HttpResponse("Error 404")

def request_view(request,*args,**kwargs):
	return HttpResponse("<h1>Request Page</h1>")