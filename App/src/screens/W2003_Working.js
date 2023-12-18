import { useState, useEffect, useCallback } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { fetchWorkingData } from '../api/W2003_WorkingData';


const W2003_Working = () => {
  const [workingData, setWorkingData] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchData = useCallback(async () => {
    try {
      const response = await fetchWorkingData();
  
      console.log('서버 응답 데이터:', response.data);
  
      if (response.status === 200 && response.data.success) {
        const formattedData = mapServerResponseToWorkingData(response.data);
        setWorkingData(formattedData);
      } else {
        console.error('서버 응답 에러:', response.status);
      }
    } catch (error) {
      console.error('네트워크 오류:', error);
    } finally {
      setLoading(false);
    }
  }, [mapServerResponseToWorkingData]);
  
  
  const mapServerResponseToWorkingData = useCallback((data) => {
    return {
      memberName: data.memberName,
      checkInDate: convertToDate(data.recentCheckIn),
      checkInTime: convertToDateTime(data.recentCheckIn),
      checkOutDate: convertToDate(data.recentCheckOut),
      checkOutTime: convertToDateTime(data.recentCheckOut),
    };
  }, []);
  
  
  
  const convertToDateTime = (dateTimeObject) => {
    if (dateTimeObject) {
      const { year, month, day, hour, minute } = dateTimeObject;
      if (hour !== undefined && minute !== undefined) {
        return `${hour}시 ${minute}분`;
      } else {
        return `${year}년 ${month}월 ${day}일`;
      }
    }
    return '날짜 및 시간 정보 없음';
  };
  
  const convertToDate = (dateTimeObject) => {
    if (dateTimeObject) {
      const { year, month, day } = dateTimeObject;
      return `${year}년 ${month}월 ${day}일`;
    }
    return '날짜 정보 없음';
  };

  
  const isLate = (checkInTime) => {
    // checkInTime 예: "9시 30분"
    const [hour, minute] = checkInTime.split('시 ');
    return parseInt(hour) > 9 || (parseInt(hour) === 9 && parseInt(minute) > 0);
  };

  useEffect(() => {
    fetchData();
  }, [fetchData, mapServerResponseToWorkingData]);

  console.log('workingData:', workingData);

  return (
      <View style={styles.container}>
      {loading ? (
        <Text>Loading...</Text>
      ) : workingData ? (
        <View>
          <Text style={{ fontSize: 24, textAlign: 'center' }}>가장 최근 출근 기록</Text>
          <View style={styles.dataContainer}>
            <View style={styles.innerContainer}>
              <Text style={{ fontSize: 20, fontWeight: '700' }}>{workingData.memberName}님</Text>
              <View style={styles.horizontalLine}/>
              <Text style={{ fontSize: 28, fontWeight: '700', textAlign: 'center' }}>{workingData.checkInTime}</Text>
              <Text style={{ fontSize: 16, fontWeight: '700', textAlign: 'center' }}>({workingData.checkInDate})</Text>
              <View style={styles.horizontalLine}/>
              <Text style={isLate(workingData.checkInTime) ? styles.late : styles.onTime}>
                {isLate(workingData.checkInTime) ? "지각하셨습니다." : "출근이 완료되었습니다."}
              </Text>
            </View>
          </View>

          {/* 퇴근 기록이 있는 경우에만 표시 */}
          {workingData.checkOutDate !== '날짜 정보 없음' && (
            <View>
              <Text style={{ fontSize: 24, textAlign: 'center' }}>가장 최근 퇴근 기록</Text>
              <View style={styles.dataContainer}>
                <View style={styles.innerContainer}>
                  <Text style={{ fontSize: 20, fontWeight: '700' }}>{workingData.memberName}님</Text>
                  <View style={styles.horizontalLine}/>
                  <Text style={{ fontSize: 28, fontWeight: '700', textAlign: 'center' }}>{workingData.checkOutTime}</Text>
                  <Text style={{ fontSize: 16, fontWeight: '700', textAlign: 'center' }}>({workingData.checkOutDate})</Text>
                  <View style={styles.horizontalLine}/>
                  <Text style={{ fontSize: 16, fontWeight: '700', textAlign: 'center', color: 'blue' }}>퇴근하셨습니다.</Text>
                </View>
              </View>
            </View>
          )}
        </View>
      ) : (
        <Text>출근 기록이 없습니다.</Text>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    padding: 40,
  },
  dataContainer: {
    marginTop: 10,
    marginBottom: 50,
    backgroundColor: '#fff',
    borderRadius: 10,
    width: 300,
    padding: 30,
    elevation: 3,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
  },
  onTime: {
    color: 'green',
    fontSize: 16,
    fontWeight: '700',
    textAlign: 'center'
  },
  late: {
    color: 'red',
    fontSize: 16, 
    fontWeight: '700',
    textAlign: 'center'
  },
  horizontalLine: {
    borderBottomColor: 'gray',
    borderBottomWidth: 1,
    width: '100%',
    margin: 10,
  },
});

export default W2003_Working;
