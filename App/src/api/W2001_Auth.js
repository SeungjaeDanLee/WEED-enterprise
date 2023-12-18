import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

export const signIn = async (email, password) => {
  try {
    const response = await axios.post(
      'http://10.100.203.31:8099/api/app/member/login',
      // 'http://15.164.62.243:8099/api/app/member/login',
      { email, password },
      {
        headers: { 'Content-Type': 'application/json' },
        timeout: 5000, // milliseconds (5 seconds)
      }
    );
    console.log('response', response);
    console.log('response.data', response.data); // 서버에서 받은 데이터

    if (response.data !== null && response.data !== '') {
      // 토큰 및 사용자 정보를 AsyncStorage에 저장
      await AsyncStorage.setItem('accessToken', response.data.accessToken);
      await AsyncStorage.setItem('refreshToken', response.data.refreshToken);
      await AsyncStorage.setItem('authority', response.data.authority);

      const storedAccessToken = await AsyncStorage.getItem('accessToken');
      const storedRefreshToken = await AsyncStorage.getItem('refreshToken');
      const storedAuthority = await AsyncStorage.getItem('authority');

      console.log('Stored AccessToken:', storedAccessToken);
      console.log('Stored RefreshToken:', storedRefreshToken);
      console.log('Stored Authority:', storedAuthority);

      return Promise.resolve({
        authority: storedAuthority,
      });
    } else {
      console.log('로그인 실패', '아이디나 비밀번호를 확인하세요.');
      // 로그인 실패 시에는 Promise를 통해 에러 메시지 반환
      return Promise.reject(new Error('아이디나 비밀번호를 확인하세요.'));
    }
  } catch (error) {
    if (axios.isCancel(error)) {
      // Timeout 에러인 경우
      console.log('서버 응답이 지연되었습니다. 잠시 후 다시 시도하세요.');
      return Promise.reject(
        new Error('서버 응답이 지연되었습니다. 잠시 후 다시 시도하세요.')
      );
    } else if (error.response && error.response.status === 401) {
      // 401 에러는 인증 실패를 의미하므로 로그인 실패 메시지 반환
      console.log('로그인 실패', '아이디나 비밀번호를 확인하세요.');
      return Promise.reject(new Error('아이디나 비밀번호를 확인하세요.'));
    } else {
      // 그 외의 경우에는 서버와의 통신 중 오류 메시지 반환
      console.log(`Error Message: ${error}`);
      return Promise.reject(new Error('서버와의 통신 중 오류가 발생했습니다.'));
    }
  }
};

export const autoSignIn = async () => {
  try {
    // AsyncStorage에서 저장된 토큰과 사용자 정보를 가져옴
    const storedAccessToken = await AsyncStorage.getItem('accessToken');
    const storedRefreshToken = await AsyncStorage.getItem('refreshToken');
    const storedAuthority = await AsyncStorage.getItem('authority');
    
    // 토큰이 존재하면 로그인 시도
    if (storedAccessToken && storedRefreshToken && storedAuthority) {
      const response = await axios.post(
        'http://10.100.203.31:8099/api/app/member/autoLogin',
        { accessToken: storedAccessToken, refreshToken: storedRefreshToken },
        {
          headers: { 'Content-Type': 'application/json' },
          timeout: 5000, // milliseconds (5 seconds)
        }
      );

      if (response.data !== null && response.data !== '') {
        // 로그인 성공 시 저장된 정보 반환
        return Promise.resolve({
          authority: storedAuthority,
        });
      }
    }

    // 저장된 토큰이 없거나 로그인 실패 시 에러 반환
    return Promise.reject(new Error('자동 로그인 실패'));
  } catch (error) {
    // 에러 핸들링 코드
  }
};