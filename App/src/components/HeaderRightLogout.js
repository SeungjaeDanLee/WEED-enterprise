import { TouchableOpacity, Text, StyleSheet } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useUserContext } from '../contexts/UserContext';

const HeaderRightLogout = () => {
  const { setUser } = useUserContext();

  const handleLogout = async () => {
    try {
      // AsyncStorage에서 토큰 제거
      await AsyncStorage.removeItem('accessToken');
      await AsyncStorage.removeItem('refreshToken');

       // 사용자 정보를 null로 설정하여 로그아웃 상태로 변경
      setUser(null);

    } catch (error) {
      console.error('로그아웃 중 에러 발생:', error.message);
    }
  };

  return (
    <TouchableOpacity onPress={handleLogout}>
      <Text style={styles.logoutText}>로그아웃</Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  logoutText: {
    textAlign: 'right',
    color: 'white',
    padding: 10,
    fontWeight: 'bold',
  },
});

export default HeaderRightLogout;
