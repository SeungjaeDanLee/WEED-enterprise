import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { PRIMARY, WHITE } from '../colors';
import HeaderLeftButton from '../components/HeaderLeftButton';
import HeaderRightButton from '../components/HeaderRightButton';
import W2002_QRcode from '../screens/W2002_QRcode';
import W2003_Working from '../screens/W2003_Working';
import HeaderRightLogout from '../components/HeaderRightLogout';

const Stack = createNativeStackNavigator();

const MainStack = () => {
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
        headerLeft: HeaderLeftButton,
        headerRight: HeaderRightButton,
      }}
    >
      <Stack.Screen
        name="QR"
        component={W2002_QRcode}
        options={{
          title: 'QR출결',
          headerRight: HeaderRightButton,
        }}
      />
      <Stack.Screen
        name="List"
        component={W2003_Working}
        options={{
          title: 'QR출결',
          headerRight: () => <HeaderRightLogout />,
        }}
      />
    </Stack.Navigator>
  );
};

export default MainStack;
