import { useState, useEffect } from 'react';
import { View, StyleSheet, Button, Alert } from 'react-native';
import { BarCodeScanner } from 'expo-barcode-scanner';
import { StatusBar } from 'expo-status-bar';
import { checkInOut } from '../api/W2004_Scanner';
// import { useUserContext } from '../contexts/UserContext';

const W2004_QRScanner = () => {
  const [hasPermission, setHasPermission] = useState(null);
  const [scanned, setScanned] = useState(false);
  // const { user, setUser } = useUserContext();

  useEffect(() => {
    (async () => {
      const { status } = await BarCodeScanner.requestPermissionsAsync();
      setHasPermission(status === 'granted');
    })();
  }, []);

  const showAlertAndReset = (title, message) => {
    Alert.alert(title, message);

    // 3초 후에 스캔 창을 다시 열기
    setTimeout(() => {
      setScanned(false);
    }, 3000);
  };

  const handleBarCodeScanned = async ({ data }) => {
    if (!hasPermission) {
      showAlertAndReset('권한 오류', '카메라 권한을 허용해주세요.');
      return;
    }

    setScanned(true);

    try {
      const response = await checkInOut(data);

      console.log('Server Response:', response.data);

      if (response.data.success) {
        if (response.data.checkIn) {
          showAlertAndReset('출근 성공', '출근에 성공했습니다.');
        } else {
          showAlertAndReset('퇴근 성공', '퇴근에 성공했습니다.');
        }
      } else {
        showAlertAndReset(
          '에러',
          response.data.error || '처리에 실패했습니다.'
        );
      }

      console.log('Scanned Data:', data);

      // setUser({ ...user, scannedData: data });

      // // 데이터를 W2002_QRcode로 직접 전달
      // user.onDataReceived && user.onDataReceived(data);

      // console.log('user.onDataReceived', user.onDataReceived);
      // console.log('user.onDataReceived(data)', user.onDataReceived(data));
    } catch (error) {
      console.error('Error:', error);
      showAlertAndReset('에러', '처리에 실패했습니다.');
    }
  };

  return (
    <View style={styles.container}>
      <BarCodeScanner
        onBarCodeScanned={scanned ? undefined : handleBarCodeScanned}
        style={StyleSheet.absoluteFillObject}
      />
      {scanned && (
        <Button title={'다시 스캔'} onPress={() => setScanned(false)} />
      )}
      <View style={{ margin: 50 }}>
        <StatusBar style="dark" />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'flex-end',
  },
});

export default W2004_QRScanner;
