package com.example.dongsan2mong.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dongsan2mong.data.MemberInfoData
import com.example.dongsan2mong.api.RetrofitBuilder
import com.example.dongsan2mong.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var backpressedTime: Long = 0
    var memberInfo = MemberInfoData()
    val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // 다시 돌아왔을 때 할 거
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.apply {

            loginNullBtn.setOnClickListener {
                getLoginInfoHttp { memberInfo ->
                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    i.putExtra("memberInfo", memberInfo)
                    startActivity(i)
                    finish()
                }
            }

            val loginIntent = Intent(this@LoginActivity, SignupActivity::class.java)
            loginKakaoBtn.setOnClickListener {
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        Log.e("LOGIN", "카카오계정으로 로그인 실패", error)

                    } else if (token != null) {
                        Log.i("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")
                        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                            UserApiClient.instance.me { user, error ->
                                Log.d(
                                    "kakaologin",
                                    "name : ${user?.kakaoAccount?.profile?.nickname}, 이메일 : ${user?.kakaoAccount?.email}"
                                )
                                memberInfo = MemberInfoData(
                                    20010304,
                                    user?.kakaoAccount?.profile?.nickname.toString(),
                                    user?.kakaoAccount?.email.toString(),
                                )
                                val i = Intent(this@LoginActivity, MainActivity::class.java)

                                i.putExtra("memberInfo", memberInfo)
                                startActivity(i)
//                                finish()
//                                    nickname.text = "닉네임: ${user?.kakaoAccount?.profile?.nickname}"
                            }
                        }
                    }
                }
                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity) { token, error ->
                        if (error != null) {
                            Log.e("LOGIN", "카카오톡으로 로그인 실패", error)

                            // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                            // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            }

                            // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                            UserApiClient.instance.loginWithKakaoAccount(
                                this@LoginActivity,
                                callback = callback
                            )
                        } else if (token != null) {
                            Log.i("LOGIN", "카카오톡으로 로그인 성공 ${token.accessToken}")
                            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                                UserApiClient.instance.me { user, error ->
                                    Log.d(
                                        "kakaologin",
                                        "name : ${user?.kakaoAccount?.profile?.nickname}, 이메일 : ${user?.kakaoAccount?.email}"
                                    )
//                                    nickname.text = "닉네임: ${user?.kakaoAccount?.profile?.nickname}"
                                }
                            }
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                            finish()
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(
                        this@LoginActivity,
                        callback = callback
                    )
                }
            }

            signupKakaoBtn.setOnClickListener {
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        Log.e("LOGIN", "카카오계정으로 로그인 실패", error)

                    } else if (token != null) {
                        Log.i("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")
                        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                            UserApiClient.instance.me { user, error ->
                                Log.d(
                                    "kakaologin",
                                    "name : ${user?.kakaoAccount?.profile?.nickname}, 이메일 : ${user?.kakaoAccount?.email}"
                                )
                                memberInfo = MemberInfoData(
                                    20010304,
                                    user?.kakaoAccount?.profile?.nickname.toString(),
                                    user?.kakaoAccount?.email.toString(),
                                )
                                val i = Intent(this@LoginActivity, SignupActivity::class.java)

                                i.putExtra("memberInfo", memberInfo)
                                startActivity(i)
//                                finish()
//                                    nickname.text = "닉네임: ${user?.kakaoAccount?.profile?.nickname}"
                            }
                        }
                    }
                }
                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity) { token, error ->
                        if (error != null) {
                            Log.e("LOGIN", "카카오톡으로 로그인 실패", error)

                            // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                            // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            }

                            // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                            UserApiClient.instance.loginWithKakaoAccount(
                                this@LoginActivity,
                                callback = callback
                            )
                        } else if (token != null) {
                            Log.i("LOGIN", "카카오톡으로 로그인 성공 ${token.accessToken}")
                            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                                UserApiClient.instance.me { user, error ->
                                    Log.d(
                                        "kakaologin",
                                        "name : ${user?.kakaoAccount?.profile?.nickname}, 이메일 : ${user?.kakaoAccount?.email}"
                                    )
//                                    nickname.text = "닉네임: ${user?.kakaoAccount?.profile?.nickname}"
                                }
                            }
                            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(
                        this@LoginActivity,
                        callback = callback
                    )
                }
            }

            loginNaverBtn.setOnClickListener {
                loginIntent.putExtra("isKakao", false)
                launcher.launch(loginIntent)

            }
        }

    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis()
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish()
        }
    }

    private fun getLoginInfoHttp(callback: (memberInfo: MemberInfoData) -> Unit) {
        val getMemberInfo = RetrofitBuilder.api.getMemberInfo(1)
        getMemberInfo.enqueue(object : Callback<MemberInfoData> {
            override fun onResponse(
                call: Call<MemberInfoData>,
                response: Response<MemberInfoData>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_LONG).show()
                    memberInfo = response.body() ?: MemberInfoData()
                    Log.d("memberInfo", memberInfo.toString())
                    callback(memberInfo)
                    // 받은 정보 메인 액티비티로 보내줄 필요
                }
            }

            override fun onFailure(call: Call<MemberInfoData>, t: Throwable) {
                Toast.makeText(applicationContext, "Call Failed", Toast.LENGTH_LONG).show()

            }

        })
    }

}