import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BACKEND_BASE_URL } from '../constants/constants';

const SOCKET_URL = `${BACKEND_BASE_URL}/ws`;

export const connect = (patientId: number, onMessageReceived: (message: any) => void) => {
  const client = new Client({
    webSocketFactory: () => new SockJS(SOCKET_URL),
    onConnect: () => {
      console.log('Connected to WebSocket');
      client.subscribe(`/topic/alarms/${patientId}`, (message) => {
        onMessageReceived(JSON.parse(message.body));
      });
    },
  });

  client.activate();
};
