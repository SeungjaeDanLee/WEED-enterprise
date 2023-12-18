import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

export const fetchWorkingData = async () => {
  try {
    // AsyncStorage에서 저장된 토큰을 가져옴
    const storedAccessToken = await AsyncStorage.getItem('accessToken');

    // 토큰이 존재할 경우에만 서버에 요청
    if (storedAccessToken) {
      const response = await axios.get(
        'http://10.100.203.31:8099/api/app/member/working',{
        // 'http://15.164.62.243:8099/api/app/member/working', {
          headers: {
            Authorization: `${storedAccessToken}`,
          },
        }
      );
      return response;
    } else {
      throw new Error('저장된 토큰이 없습니다.');
    }
  } catch (error) {
    console.error('네트워크 오류:', error);
    throw error;
  }
};
