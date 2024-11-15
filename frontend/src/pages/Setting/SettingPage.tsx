import Header from '@components/Header/Header';
import { WebhookSettings } from '@components/Setting/WebhookSettings';

const SettingsPage = () => {
  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6 ">
      <Header title="Setting" />
      <WebhookSettings />
    </div>
  );
};

export default SettingsPage;
