import { setupWorker } from 'msw';
import { handlers } from './bestMergeRequest.ts';
// import { handlers } from './gitlab';
// import { handlers } from './handlers';

export const worker = setupWorker(...handlers);
