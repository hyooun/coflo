export class ProjectIdError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'ProjectIdError';
  }
}
