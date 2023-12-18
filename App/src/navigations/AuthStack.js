import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { PRIMARY, WHITE } from '../colors';
import W2001_SignIn from '../screens/W2001_SignIn';
import { useUserContext } from '../contexts/UserContext';
import { useEffect } from 'react';
import { Alert } from 'react-native';
import W2004_QRScanner from '../screens/W2004_QRScanner';

const Stack = createNativeStackNavigator();

const AuthStack = () => {
  const { user } = useUserContext();

  useEffect(() => {
    // GUEST로 로그인 시 토큰 발급 없이 알림 표시
    if (user && user.authority === 'GUEST') {
      user == null;
      // 알림 표시
      Alert.alert('가입 대기 중', '관리자에게 문의하시기 바랍니다.');
    }
  }, [user]);

  return (
    <Stack.Navigator
      screenOptions={{
        contentStyle: { backgroundColor: WHITE },
        headerStyle: { backgroundColor: PRIMARY.DEFAULT },
        headerTitleAlign: 'center',
        headerTintColor: WHITE,
        headerTitleStyle: {
          fontWeight: '700',
        },
      }}
    >
      <Stack.Screen
        name="SignIn"
        component={W2001_SignIn}
        options={{
          headerShown: false,
        }}
      />
      <Stack.Screen name="QRScanner" component={W2004_QRScanner} />
    </Stack.Navigator>
  );
};

export default AuthStack;
