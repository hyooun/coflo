import { useEffect, useState } from 'react';
import { WebhookChannel } from 'types/channel';
import { ChannelCode } from 'types/channel';
import { Channels } from '@apis/Channels';

import { useAtom } from 'jotai';
import { projectIdAtom } from '@store/auth';
import AlertModal from '@components/Modal/AlertModal';
import { TriangleAlert } from 'lucide-react';
import { CircleCheck } from 'lucide-react';

export const WebhookSettings = () => {
  const [channels, setChannels] = useState<ChannelCode[]>([]);
  const [webhookChannels, setWebhookChannels] = useState<WebhookChannel[]>([]);
  const [urls, setUrls] = useState<{ [key: string]: string }>({
    Mattermost: '',
    Discord: '',
  });
  const [isAlertModalOpen, setIsAlertModalOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState<string[]>([]);
  const [isSuccess, setIsSuccess] = useState(false);
  const [projectId] = useAtom(projectIdAtom);

  useEffect(() => {
    // 채널 목록 가져오기
    const fetchChannelCodeList = async () => {
      const response = await Channels.getChannelCodeList();
      setChannels(response.data ?? []);
    };

    // 웹훅 채널 목록 가져오기
    const fetchWebhookChannelList = async () => {
      if (!projectId) return;
      const response = await Channels.getWebhookChannelList(Number(projectId));
      setWebhookChannels(response.data ?? []);

      console.log('webhookChannels : ', webhookChannels);

      // 기존 웹훅 URL 설정
      const initialUrls =
        response.data?.reduce((acc: any, channel: WebhookChannel) => {
          acc[channel.channelName] = channel.webhookUrl;
          return acc;
        }, {}) ?? {};
      setUrls(initialUrls);
    };

    fetchChannelCodeList();
    fetchWebhookChannelList();
  }, [projectId]);

  const handleUrlChange = (channelName: string, url: string) => {
    setUrls((prevUrls) => ({ ...prevUrls, [channelName]: url }));
  };

  const handleSetClick = async (channel: ChannelCode) => {
    if (!projectId) return;
    if (!urls[channel.channelName] || urls[channel.channelName].length <= 0) {
      setAlertMessage(['웹훅 URL을 입력해주세요.']);
      setIsSuccess(false);
      setIsAlertModalOpen(true);
      return;
    }

    try {
      await Channels.addWebhookChannel(
        Number(projectId),
        channel.channelCodeId,
        urls[channel.channelName] || '',
      );
      // 웹훅 채널 목록 새로고침
      const response = await Channels.getWebhookChannelList(Number(projectId));
      setWebhookChannels(response.data ?? []);

      setAlertMessage(['웹훅 설정이 완료되었습니다.']);
      setIsSuccess(true);
      setIsAlertModalOpen(true);
    } catch (error) {
      console.error('웹훅 설정 실패:', error);
      setAlertMessage(['웹훅 설정에 실패하였습니다.']);
      setIsSuccess(false);
      setIsAlertModalOpen(true);
    }
  };

  const handleTestClick = async (channel: ChannelCode) => {
    if (!projectId) return;
    try {
      await Channels.testWebhookMessage(Number(projectId), channel.channelCodeId);
      console.log('테스트 메시지 전송 완료');
    } catch (error) {
      console.error('테스트 메시지 전송 실패:', error);
    }
  };

  return (
    <div className="p-4">
      <div className="flex flex-row items-center gap-2 min-w-[200px] mb-4">
        <div className="w-4 h-4 bg-primary-500 rounded-full"></div>
        <h2 className="text-lg font-bold">웹훅 설정</h2>
      </div>
      {channels.map((channel) => (
        <div key={channel.channelCodeId} className="mb-6">
          <div className="flex items-center mb-2">
            {/* <img src={channel.logo} alt={`${channel.channelName} logo`} className="w-6 h-6 mr-2" /> */}
            <span className="font-semibold">{channel.channelName}</span>
          </div>
          <div className="flex flex-row items-center justify-between gap-4">
            <input
              type="text"
              placeholder="URL 입력..."
              value={urls[channel.channelName] || ''}
              onChange={(e) => handleUrlChange(channel.channelName, e.target.value)}
              className="p-2 px-4 bg-background-bnavy rounded-2xl w-full min-w-[200px] focus:outline-none"
            />
            <div className="flex flex-row gap-4">
              <button
                onClick={() => handleSetClick(channel)}
                className="bg-primary-500 text-white rounded-2xl w-full whitespace-nowrap p-2 px-4"
              >
                설정
              </button>
              <button
                onClick={() => handleTestClick(channel)}
                className="bg-primary-500 text-white rounded-2xl w-full whitespace-nowrap p-2 px-4"
              >
                테스트 메시지
              </button>
            </div>
          </div>
        </div>
      ))}

      {isAlertModalOpen && (
        <AlertModal
          content={alertMessage}
          onConfirm={() => setIsAlertModalOpen(false)}
          icon={isSuccess ? CircleCheck : TriangleAlert}
          iconClassName={isSuccess ? 'text-state-success' : 'text-state-warning'}
        />
      )}
    </div>
  );
};
