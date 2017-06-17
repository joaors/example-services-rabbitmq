import { AlunosAppPage } from './app.po';

describe('alunos-app App', () => {
  let page: AlunosAppPage;

  beforeEach(() => {
    page = new AlunosAppPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
