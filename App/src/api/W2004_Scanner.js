import axios from 'axios';

export const checkInOut = async (token) => {
  try {
    const response = await axios.post(
      'http://10.100.203.31:8099/api/app/member/checkInOut',
      // 'http://15.164.62.243:8099/api/app/member/checkInOut',
      { token },
      { headers: { 'Content-Type': 'application/json' } }
    );

    return response;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};