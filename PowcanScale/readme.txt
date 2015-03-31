//		new Thread(){
//			public void run() {
//				if (!mSpUtil.isLogin()) {
//					GATRequest gat = new GATRequest(6);
//					GATResponse response = (GATResponse) NetRequest.getInstance(getActivity()).send(gat, GATResponse.class);
//					if (response != null) {
//						List<Integer> noList = response.BKH;
//						if (noList != null && noList.size() > 0) {
//							Integer account = noList.get(1);
//							int height = 178;
//							
//							REGRequest reg = new REGRequest();
//							reg.account = account + "";
//							reg.pswd = Md5Utils.encryptMD5("123456");
//							reg.imei = Utils.getDeviceId(getActivity());
//							reg.gender = "M";
//							reg.birthday = "19880722";
//							reg.height = height + "";
//							reg.phone = "13424269212";
//							reg.qq = "442666876";
//							reg.email = "442666876@qq.com";
//							
//							BaseResponse regResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(reg, BaseResponse.class);
//							if (regResponse != null && regResponse.RES == 201) {
//								mSpUtil.setLogin(true);
//							}
//						}
//					}
//				} else {
//					LGNRequest request = new LGNRequest();
//					request.number = "2001978";
//					request.pswd = Md5Utils.encryptMD5("123456");
//					
//					LGNResponse response = NetRequest.getInstance(getActivity()).send(request, LGNResponse.class);
//					if (response != null && response.RES == 301) {
//						mSpUtil.setLogin(true);
//						//mSpUtil.setAccount(response.ACT);
//						mSpUtil.setGender(response.GDR);
//						mSpUtil.setBirthday(response.GDR);
//						//mSpUtil.setHeight(response.HET);
//						mSpUtil.setPhone(response.PHN);
//						mSpUtil.setQQ(response.QQN);
//						mSpUtil.setEmail(response.EML);
//					}
//				} else {
//					int account = mSpUtil.getAccount();
//					int height = 180;
//					
//					UTURequest utu = new UTURequest();
//					utu.account = account + "";
//					utu.pswd = Md5Utils.encryptMD5("123456");
//					utu.imei = Utils.getDeviceId(getActivity());
//					utu.gender = "M";
//					utu.birthday = "19880722";
//					utu.height = height + "";
//					utu.phone = "13424269212";
//					utu.qq = "442666876";
//					utu.email = "442666876@qq.com";
//					
//					BaseResponse regResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(utu, BaseResponse.class);
//					if (regResponse != null && regResponse.RES == 401) {
//						showToast("更新资料成功");
//					}
					
//					GTNRequest gtn = new GTNRequest();
////					gtn.number = "13424269212";
////					gtn.type = "phone";
//					gtn.number = "442666876";
//					gtn.type = "qq";
////					gtn.number = "442666876@qq.com";
////					gtn.type = "email";
//					
//					GTNResponse gtnResponse = (GTNResponse) NetRequest.getInstance(getActivity()).send(gtn, GTNResponse.class);
//					if (gtnResponse != null && gtnResponse.RES == 501) {
//						showToast("获取成功");
//					}
					
//					int account = mSpUtil.getAccount();
//					
//					FBPRequest fbp = new FBPRequest();
//					fbp.account = account + "";
//					fbp.number = "442666876";
//					fbp.type = "qq";
//					
//					BaseResponse fbpResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(fbp, BaseResponse.class);
//					if (fbpResponse != null && fbpResponse.RES == 601) {
////						showToast("验证成功，允许修改密码");
//					}
					
//					int account = mSpUtil.getAccount();
//					
//					UPPRequest upp = new UPPRequest();
//					upp.account = account + "";
//					upp.password = Md5Utils.encryptMD5("654321");
//					
//					BaseResponse uppResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(upp, BaseResponse.class);
					
//					int account = mSpUtil.getAccount();
//					
//					LGNRequest request = new LGNRequest();
//					request.number = account + "";
//					request.pswd = Md5Utils.encryptMD5("654321");
//					
//					LGNResponse response = NetRequest.getInstance(getActivity()).send(request, LGNResponse.class);
					
//					int account = mSpUtil.getAccount();
//					
//					CHPRequest chp = new CHPRequest();
//					chp.account = account + "";
//					chp.oldpsw = Md5Utils.encryptMD5("654321");
//					chp.newpsw = Md5Utils.encryptMD5("123456");
//					
//					BaseResponse chpResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(chp, BaseResponse.class);
					
//					int account = mSpUtil.getAccount();
//					
//					LGNRequest request = new LGNRequest();
//					request.number = account + "";
//					request.pswd = Md5Utils.encryptMD5("123456");
//					
//					LGNResponse response = NetRequest.getInstance(getActivity()).send(request, LGNResponse.class);
					
//					int account = mSpUtil.getAccount();
//					
//					RECRequest request = new RECRequest();
//					request.account = account + "";
//					request.weight = "11.1";
//					request.fat = "11.123456";
//					request.water = "11.1";
//					request.muscle = "11.1";
//					request.bone = "11.1";
//					request.bmr = "11.1";
//					request.sfat = "11.1";
//					request.infat = "11.1";
//					request.bodyage = "11.1";
//					request.amr = "11.1";
//					
//					LGNResponse response = NetRequest.getInstance(getActivity()).send(request, LGNResponse.class);
//				}
//			};
//		}.start();