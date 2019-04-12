from django.shortcuts import render, get_object_or_404
from django.http import Http404
from .models import Request
# Create your views here.
def get_requests_view(request,num):
	try:
		reqs = Request.objects.get(id=num)
	except Request.DoesNotExist:
		raise Http404
	#reqs = get_object_or_404(Request,id=num)#lo mismo
	context = {"id" : reqs.id_request,
				"mobile" : reqs.mobile}
	return render(request,"requests/requests.html",context)

def get_all_request_view(request):
	lrequest = Request.objects.all()
	context = {"list" : lrequest}
	return render(request,"requests/allrequests.html",context)